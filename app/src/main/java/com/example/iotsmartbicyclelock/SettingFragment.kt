package com.example.iotsmartbicyclelock

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.iotsmartbicyclelock.databinding.FragmentSettingBinding

class SettingFragment:Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


//    // import한 BluetoothSPP 변수 선언
//    private var bt: BluetoothSPP? = null
    // 설정완료 이미지 flag
    private var flag:Boolean = false

    // sound
    var sound:Int = 0
    // light
    var light:Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.soundSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.soundTVResult.text = progress.toString()
                sound = progress*10
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.lightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.lightTVResult.text = progress.toString()
                light = progress*100
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

//
//        // 소리 & 빛 설정 값 sharedPreference로 home으로 넘기기
//        binding.completed.setOnClickListener {
//
//            Log.e("setting_sound",sound.toString())
//
//            flag = !flag
//            if(flag){
//                binding.completed.setBackgroundColor(resources.getColor(R.color.purple_700))
//            }else{
//                binding.completed.setBackgroundColor(resources.getColor(R.color.black))
//            }
//
//            MyApplication.prefs.setString("sound",sound.toString())
//            MyApplication.prefs.setString("light",light.toString())
//        }



//        // 객체 생성 후 미리 선언한 변수에 넣음
//        bt = BluetoothSPP(requireContext()) //Initializing
//        if (!bt!!.isBluetoothAvailable) { //블루투스 사용 불가라면
//            // 사용불가라고 토스트 띄워줌
//            Toast.makeText(
//                context, "Bluetooth is not available", Toast.LENGTH_SHORT
//            ).show()
//            // 화면 종료
//            activity?.supportFragmentManager
//                ?.beginTransaction()
//                ?.remove(this)
//                ?.commit()
//        }



//        // 데이터를 받았는지 감지하는 리스너
//        bt!!.setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener { data, message ->
//        })
//
//
//
//        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
//        bt!!.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
//            //연결됐을 때
//            override fun onDeviceConnected(name: String, address: String) {
//
//            }
//
//            override fun onDeviceDisconnected() { //연결해제
//
//            }
//
//            override fun onDeviceConnectionFailed() { //연결실패
//
//            }
//        })



//        // 연결하는 기능 버튼 가져와서 이용하기
//        // 연결시도
//        // 버튼 클릭하면
//
//             fun onClick(v: View) {
//                if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
//                    bt!!.disconnect()
//                } else {
//                    val intent = Intent(requireContext(), DeviceList::class.java)
//                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
//                }
//            }


        return root
    }

//    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
//    public override fun onDestroy() {
//        super.onDestroy()
//        bt!!.stopService() //블루투스 중지
//    }


//    // 앱이 시작하면
//    public override fun onStart() {
//        super.onStart()
//        if (!bt!!.isBluetoothEnabled) { // 앱의 상태를 보고 블루투스 사용 가능하면
//            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            // 새로운 액티비티 띄워줌, 거기에 현재 가능한 블루투스 정보 intent로 넘겨
//            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
//        } else {
//            if (!bt!!.isServiceAvailable) { // 블루투스 사용 불가
//                // setupService() 실행하도록
//                bt!!.setupService()
//                bt!!.startService(BluetoothState.DEVICE_OTHER) //DEVICE_ANDROID는 안드로이드 기기끼리
//                // 셋팅 후 연결되면 setup()으로
//                setup()
//            }
//        }
//    }


//    // 블루투스 사용 - 데이터 전송
//    fun setup() {
//
//        binding.soundSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                binding.soundTVResult.text = progress.toString()
//                sound = progress*10
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//
//            }
//        })
//
//        binding.lightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                binding.lightTVResult.text = progress.toString()
//                light = progress*100
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//
//            }
//        })
//
//
//    }
//
//
//
//    // 새로운 액티비티 (현재 액티비티의 반환 액티비티?)
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
//            if (resultCode == AppCompatActivity.RESULT_OK) // 연결됨
//                bt!!.connect(data)
//        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
//            if (resultCode == AppCompatActivity.RESULT_OK) { // 연결됨
//                bt!!.setupService()
//                bt!!.startService(BluetoothState.DEVICE_OTHER)
//                setup()
//            } else { // 사용불가
//                Toast.makeText(
//                    context, "Bluetooth was not enabled.", Toast.LENGTH_SHORT
//                ).show()
//                activity?.supportFragmentManager
//                    ?.beginTransaction()
//                    ?.remove(this)
//                    ?.commit()
//            }
//        }
//    }
}