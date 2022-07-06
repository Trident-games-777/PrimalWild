package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.wrappers

import android.content.Context
import androidx.core.net.toUri
import com.appsflyer.AppsFlyerLib
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.CompleteData
import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data.Const
import java.util.*

fun createUrl(data: CompleteData, base: String, context: Context): String {
    return base.toUri().buildUpon().apply {
        appendQueryParameter(Const.SECURE_GET_PARAMETR, Const.SECURE_KEY)
        appendQueryParameter(Const.DEV_TMZ_KEY, TimeZone.getDefault().id)
        appendQueryParameter(Const.GADID_KEY, data.googleId.first)
        appendQueryParameter(Const.DEEPLINK_KEY, data.deepLink.first)
        appendQueryParameter(Const.SOURCE_KEY, data.appsData.first?.get("media_source").toString())
        appendQueryParameter(
            Const.AF_ID_KEY,
            AppsFlyerLib.getInstance().getAppsFlyerUID(context)
        )
        appendQueryParameter(Const.ADSET_ID_KEY, data.appsData.first?.get("adset_id").toString())
        appendQueryParameter(Const.CAMPAIGN_ID_KEY, data.appsData.first?.get("campaign_id").toString())
        appendQueryParameter(Const.APP_CAMPAIGN_KEY, data.appsData.first?.get("campaign").toString())
        appendQueryParameter(Const.ADSET_KEY, data.appsData.first?.get("adset").toString())
        appendQueryParameter(Const.ADGROUP_KEY, data.appsData.first?.get("adgroup").toString())
        appendQueryParameter(Const.ORIG_COST_KEY, data.appsData.first?.get("orig_cost").toString())
        appendQueryParameter(Const.AF_SITEID_KEY, data.appsData.first?.get("af_siteid").toString())
    }.toString()
}