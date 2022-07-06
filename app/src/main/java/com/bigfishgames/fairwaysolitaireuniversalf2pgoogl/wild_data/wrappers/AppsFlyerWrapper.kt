package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers

import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.AppsType

class AppsFlyerWrapper(private val doOnResult: (AppsType) -> Unit) : AppsFlyerConversionListener {
    override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
        doOnResult(p0)
    }

    override fun onConversionDataFail(p0: String?) {
        Log.e("Apps", "onConversionDataFail: $p0")
    }

    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
        Log.d("Apps", "onAppOpenAttribution: $p0")
    }

    override fun onAttributionFailure(p0: String?) {
        Log.e("Apps", "onAttributionFailure: $p0")
    }
}