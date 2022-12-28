package com.example.iotsmartbicyclelock

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.launch

class MainActivity() : AppCompatActivity() {
//    private fun asyncPermissionCheck() = lifecycleScope.launch{
//        TedPermission.create()
//            .setPermissions(
////                Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                Manifest.permission.ACCESS_FINE_LOCATION,
////                Manifest.permission.BLUETOOTH_SCAN,
////                Manifest.permission.BLUETOOTH_ADVERTISE,
//                Manifest.permission.BLUETOOTH_CONNECT
//            )
//            .check()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        requestPermission {
//            todo()
//        }


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_setting))
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//
//    private fun todo(){
//        // TODO : 기능 구현
//        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun requestPermission(logic : () -> Unit){
//        TedPermission.create()
//            .setPermissionListener(object : PermissionListener {
//                override fun onPermissionGranted() {
//                    logic()
//                }
//                override fun onPermissionDenied(deniedPermissions: List<String>) {
//                    Toast.makeText(this@MainActivity,
//                        "권한을 허가해주세요.",
//                        Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
//            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_CALENDAR )
//            .check()
    }

