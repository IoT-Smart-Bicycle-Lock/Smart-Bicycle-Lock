package com.example.iotsmartbicyclelock

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.example.iotsmartbicyclelock.databinding.FragmentLockBinding
import com.example.iotsmartbicyclelock.sharedPreferences.MyApplication

class LockFragment:Fragment() {

    private var _binding:FragmentLockBinding?=null
    private val binding get() = _binding!!

    // import한 BluetoothSPP 변수 선언
    private var bt: BluetoothSPP? = null
    // 자물쇠 잠금 이미지 flag
    private var flag:Boolean = false

    // 마그네틱 임시 테스트 flag
    private var magnetic:Boolean = false

    // 소리, 빛
    var sound:String = "0"
    var light:String = "0"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockBinding.inflate(inflater,container,false)
        val root:View = binding.root





        Log.e("now","여기는 onCreateView")




        // 객체 생성 후 미리 선언한 변수에 넣음
        bt = BluetoothSPP(requireContext()) //Initializing
        if (!bt!!.isBluetoothAvailable) { //블루투스 사용 불가라면
            // 사용불가라고 토스트 띄워줌
            Toast.makeText(
                context, "Bluetooth is not available", Toast.LENGTH_SHORT
            ).show()
            // 화면 종료
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }


//        binding.btnMagnetic.setOnClickListener {
//
//            magnetic = magnetic==false
//
//            if(magnetic){
//                binding.btnMagnetic.setBackgroundColor(resources.getColor(R.color.purple_200))
//                binding.btnMagnetic.setText("자석 떨어짐")
//
//            }else{
//                binding.btnMagnetic.setBackgroundColor(resources.getColor(R.color.orange))
//                binding.btnMagnetic.setText("자석 붙어있음")
//            }
//
//            bt!!.send("44", true)
//        }



        // light seekbar
        binding.soundSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.soundTVResult.text = progress.toString()
                sound = (progress*10).toString()
                bt!!.send((progress*10).toString(),true)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.lightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.lightTVResult.text = progress.toString()
                light = (progress*100).toString()
                bt!!.send((progress*100).toString(),true)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })






        // 데이터를 받았는지 감지하는 리스너
        bt!!.setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener { data, message ->


            //데이터 수신되면
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show() // 토스트로 데이터 띄움


            if (message == "0") {
                binding.btnCheckSafety.setBackgroundColor(resources.getColor(R.color.red))
                binding.btnCheckSafety.setText("털림")
            } else {
                binding.btnCheckSafety.setBackgroundColor(resources.getColor(R.color.green))
                binding.btnCheckSafety.setText("잠금")
            }



        })



        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt!!.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            //연결됐을 때
            override fun onDeviceConnected(name: String, address: String) {
                Toast.makeText(
                    context, "Connected to $name\n$address", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceDisconnected() { //연결해제
                Toast.makeText(
                    context, "Connection lost", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceConnectionFailed() { //연결실패
                Toast.makeText(
                    context, "Unable to connect", Toast.LENGTH_SHORT
                ).show()
            }
        })



        // 연결하는 기능 버튼 가져와서 이용하기
        // 연결시도
        // 버튼 클릭하면
        binding.btnConnect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                    bt!!.disconnect()
                } else {
                    val intent = Intent(requireContext(), DeviceList::class.java)
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
                }
            }
        })

        return root
    }

    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public override fun onDestroy() {
        super.onDestroy()
        bt!!.stopService() //블루투스 중지
    }


    // 앱이 시작하면
    public override fun onStart() {


        Log.e("now","여기는 onStart")




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
        //데이터 전송
        binding.btnLock.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // 자물쇠 이미지 설정
                if(flag){
                    binding.btnLock.setImageResource(R.drawable.lock)
                    flag=false
                }
                else{
                    binding.btnLock.setImageResource(R.drawable.unlock)
                    flag = true
                }
                bt!!.send("5", true)
            }
        })




        sound = MyApplication.prefs.getString("sound","0")

//        binding.btnSoundApply.setOnClickListener {
//            bt!!.send(sound, true)
//            Log.e("home_sound",sound)
//        }

//        binding.btnSoundOff.setOnClickListener {
//            bt!!.send("100",true)
//        }

    }



    // 새로운 액티비티 (현재 액티비티의 반환 액티비티?)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.e("now","여기는 onActivityResult")


        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == AppCompatActivity.RESULT_OK) // 연결됨
                bt!!.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == AppCompatActivity.RESULT_OK) { // 연결됨
                bt!!.setupService()
                bt!!.startService(BluetoothState.DEVICE_OTHER)
                setup()
            } else { // 사용불가
                Toast.makeText(
                    context, "Bluetooth was not enabled.", Toast.LENGTH_SHORT
                ).show()
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
            }
        }
    }

}