package com.example.hasyantestpermissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hasyantestpermissions.databinding.ActivityMainBinding
import com.example.hasyantestpermissions.ui.theme.HasyanTestPermissionsTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPermissionCamera.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) { //Проверка разрешения
                onCameraPermissionGranted()
            } else{ // Запрос разрешения
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    RQ_PERMISSIONS_FOR_FEATURE_CAMERA
                )
            }

        }

        binding.buttonPermissionAudioLocation.setOnClickListener {

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RQ_PERMISSIONS_FOR_FEATURE_CAMERA -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onCameraPermissionGranted()
                }
                else{ // Это если вдруг пользователь отклонил не навсегда, и мы можем дать доп объяснения зачем нужно одобрить разрешение
                    if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        askForOpeningPermissionSetting()
                    }
                    else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun askForOpeningPermissionSetting(){
        val appSettingIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        if(packageManager.resolveActivity(appSettingIntent, PackageManager.MATCH_DEFAULT_ONLY) == null){
            Toast.makeText(this, "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Permissions denied")
                .setMessage("You have denied permisstion forever. You can change permissions")
                .setPositiveButton("Open") { _, _ ->
                    startActivity(appSettingIntent)
                }
                .create()
                .show()
        }
    }

    private fun onCameraPermissionGranted(){
        Toast.makeText(this, "Camera Perimission Complete", Toast.LENGTH_SHORT).show()
    }

    private companion object{
        const val RQ_PERMISSIONS_FOR_FEATURE_CAMERA = 1
        const val RQ_PERMISSIONS_FOR_FEATURE_AUDIO_LOCATION = 2
    }
}



