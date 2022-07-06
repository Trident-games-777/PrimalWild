package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers

import android.content.Context
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.CompleteData
import com.onesignal.OneSignal

private const val ONE_SIGNAL_ID = "e5ad322e-56b8-4df9-84d4-a664ee8185d5"

fun notify(data: CompleteData, context: Context) {
    OneSignal.initWithContext(context)
    OneSignal.setAppId(ONE_SIGNAL_ID)
    OneSignal.setExternalUserId(data.googleId.first)

    val campaign = data.appsData.first?.get("campaign").toString()

    if (campaign == "null" && (data.deepLink.first == "null")) {
        OneSignal.sendTag("key2", "organic")
    } else if (data.deepLink.first != "null") {
        OneSignal.sendTag("key2", data.deepLink.first.replace("myapp://", "").substringBefore("/"))
    } else if (campaign != "null") {
        OneSignal.sendTag("key2", campaign.substringBefore("_"))
    }
}