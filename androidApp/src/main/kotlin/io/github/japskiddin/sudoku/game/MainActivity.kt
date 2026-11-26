package io.github.japskiddin.sudoku.game

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.japskiddin.sudoku.game.di.appGraph
import io.github.japskiddin.sudoku.game.ui.SplashViewModel

class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.isShowSplashScreen.value
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SudokuApp(
                graph = appGraph(),
                version = BuildConfig.VERSION_NAME,
                onRateApp = ::openPlayListing,
                onShareApp = ::sharePlayListing,
            )
        }
    }

    private fun openPlayListing(): Boolean {
        val listing = Intent(Intent.ACTION_VIEW, PlayListingUrl.toUri())
        return try {
            startActivity(Intent(listing).setPackage("com.android.vending"))
            true
        } catch (_: ActivityNotFoundException) {
            try {
                startActivity(listing)
                true
            } catch (_: ActivityNotFoundException) {
                false
            }
        }
    }

    private fun sharePlayListing() {
        val send = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, PlayListingUrl)
        startActivity(Intent.createChooser(send, null))
    }
}

private const val PlayListingUrl = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
