package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_data

import com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.AppsType

typealias Present<T> = Pair<T, Boolean>

data class CompleteData(
    val appsData: Present<AppsType> = Pair(null, false),
    val deepLink: Present<String> = Pair("", false),
    val googleId: Present<String> = Pair("", false)
)

fun CompleteData.isComplete(): Boolean = appsData.second && deepLink.second && googleId.second
