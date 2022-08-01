package com.example.iotsmartbicyclelock

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList

class MainActivity() : AppCompatActivity() {
    // import한 BluetoothSPP 변수 선언
    private var bt: BluetoothSPP? = null
    // 자물쇠 잠금 이미지 flag
    private var flag:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 객체 생성 후 미리 선언한 변수에 넣음
        bt = BluetoothSPP(this) //Initializing
        if (!bt!!.isBluetoothAvailable) { //블루투스 사용 불가라면
            // 사용불가라고 토스트 띄워줌
            Toast.makeText(
                applicationContext, "Bluetooth is not available", Toast.LENGTH_SHORT
            ).show()
            // 화면 종료
            finish()
        }

        // 데이터를 받았는지 감지하는 리스너
        bt!!.setOnDataReceivedListener(OnDataReceivedListener { data, message ->

            //데이터 수신되면
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show() // 토스트로 데이터 띄움

            val sample = findViewById<Button>(R.id.btnSample)
            if(message == "1"){
                sample.setBackgroundColor(resources.getColor(R.color.red))
                sample.setText("털림")
            }else{
                sample.setBackgroundColor(resources.getColor(R.color.green))
                sample.setText("안전")
            }

        })

        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt!!.setBluetoothConnectionListener(object : BluetoothConnectionListener {
            //연결됐을 때
            override fun onDeviceConnected(name: String, address: String) {
                Toast.makeText(
                    applicationContext, "Connected to $name\n$address", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceDisconnected() { //연결해제
                Toast.makeText(
                    applicationContext, "Connection lost", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceConnectionFailed() { //연결실패
                Toast.makeText(
                    applicationContext, "Unable to connect", Toast.LENGTH_SHORT
                ).show()
            }
        })
        // 연결하는 기능 버튼 가져와서 이용하기
        val btnConnect = findViewById<Button>(R.id.btnConnect) //연결시도
        // 버튼 클릭하면
        btnConnect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                    bt!!.disconnect()
                } else {
                    val intent = Intent(applicationContext, DeviceList::class.java)
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
                }
            }
        })

//
//        val sample = findViewById<Button>(R.id.btnSample)
//        sample.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(p0: View?) {
//                Toast.makeText(this@MainActivity,"sample",Toast.LENGTH_SHORT)
//            }
//
//        })
    }

    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public override fun onDestroy() {
        super.onDestroy()
        bt!!.stopService() //블루투스 중지
    }

    // 앱이 시작하면
    public override fun onStart() {
        super.onStart()
        if (!bt!!.isBluetoothEnabled) { // 앱의 상태를 보고 블루투스 사용 가능하면
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            // 새로운 액티비티 띄워줌, 거기에 현재 가능한 블루투스 정보 intent로 넘겨
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
        } else {
            if (!bt!!.isServiceAvailable) { // 블루투스 사용 불가
                // setupService() 실행하도록
                bt!!.setupService()
                bt!!.startService(BluetoothState.DEVICE_OTHER) //DEVICE_ANDROID는 안드로이드 기기끼리
                // 셋팅 후 연결되면 setup()으로
                setup()
            }
        }
    }

    // 블루투스 사용 - 데이터 전송
    fun setup() {
        val btnSend = findViewById<ImageView>(R.id.btnSend) //데이터 전송
        btnSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // 자물쇠 이미지 설정
                if(flag){
                    btnSend.setImageResource(R.drawable.lock)
                    flag=false
                }
                else{
                    btnSend.setImageResource(R.drawable.unlock)
                    flag = true
                }
                bt!!.send("5", true)
            }
        })

        val my_seekBar = findViewById<SeekBar>(R.id.seek_bar)
        val my_text  = findViewById<TextView>(R.id.TV)
        my_seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                my_text.text = progress.toString()
                bt!!.send((progress*10).toString(),true)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })


        val sound_off = findViewById<Button>(R.id.btnSoundOff)
        sound_off.setOnClickListener {
            bt!!.send("100",true)
        }

    }

    // 새로운 액티비티 (현재 액티비티의 반환 액티비티?)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == RESULT_OK) // 연결됨
                bt!!.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == RESULT_OK) { // 연결됨
                bt!!.setupService()
                bt!!.startService(BluetoothState.DEVICE_OTHER)
                setup()
            } else { // 사용불가
                Toast.makeText(
                    applicationContext, "Bluetooth was not enabled.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}