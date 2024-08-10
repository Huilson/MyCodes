package com.example.minhabomba2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.minhabomba2.ui.NavGraphic
import com.example.minhabomba2.ui.theme.MinhaBomba2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinhaBomba2Theme {
                NavGraphic()
            }
        }
    }
}