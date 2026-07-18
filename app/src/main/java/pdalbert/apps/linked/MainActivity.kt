package pdalbert.apps.linked

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pdalbert.apps.linked.ui.navigation.NavGraph
import pdalbert.apps.linked.ui.theme.LinkedTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LinkedTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
