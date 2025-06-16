package br.com.printk.ufmtnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.printk.ufmtnews.navigator.NavigationRoot
import br.com.printk.ufmtnews.ui.theme.UfmtNewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { UfmtNewsTheme { NavigationRoot() } }
    }
}
