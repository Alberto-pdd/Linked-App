package pdalbert.apps.linked

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.repository.DataStoreFolderRepository
import pdalbert.apps.linked.data.repository.DataStoreLinkRepository
import pdalbert.apps.linked.ui.navigation.NavGraph
import pdalbert.apps.linked.ui.theme.LinkedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = SessionManager(applicationContext)
        val linkRepository = DataStoreLinkRepository(applicationContext)
        val folderRepository = DataStoreFolderRepository(applicationContext)

        setContent {
            LinkedTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    sessionManager = sessionManager,
                    linkRepository = linkRepository,
                    folderRepository = folderRepository
                )
            }
        }
    }
}
