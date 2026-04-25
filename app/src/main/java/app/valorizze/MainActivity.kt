package app.valorizze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.valorizze.authentication.presenter.navigation.host.AuthenticationNavHost
import app.valorizze.design.presenter.theme.HelloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloTheme {
                AuthenticationNavHost()
            }
        }
    }
}

