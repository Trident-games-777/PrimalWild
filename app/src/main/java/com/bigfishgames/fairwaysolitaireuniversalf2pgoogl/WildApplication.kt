package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerLib
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers.AppsFlyerWrapper

typealias AppsType = MutableMap<String, Any>?

private const val APPS_ID = "QorQgyeMcCL6hNvhvje36"

class WildApplication : Application() {
    private val _appsResult: MutableLiveData<AppsType> = MutableLiveData()
    val appsResult: LiveData<AppsType> = _appsResult

    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance()
            .init(APPS_ID, AppsFlyerWrapper { _appsResult.postValue(it) }, this)
        AppsFlyerLib.getInstance().start(this)
    }
}