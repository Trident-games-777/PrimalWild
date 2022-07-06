package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl

import android.content.Context
import android.provider.Settings
import java.io.File

object Settings {
    fun blocked(context: Context): Boolean {
        return block1(context) || block2()
    }

    private fun block1(context: Context): Boolean {
        return Settings.Global.getString(
            context.contentResolver,
            Settings.Global.ADB_ENABLED
        ) == "1"
    }

    private fun block2(): Boolean {
        val places = arrayOf(
            "/sbin/", "/system/bin/", "/system/xbin/",
            "/data/local/xbin/", "/data/local/bin/",
            "/system/sd/xbin/", "/system/bin/failsafe/",
            "/data/local/"
        )
        try {
            for (where in places) {
                if (File(where + "su").exists()) return true
            }
        } catch (ignore: Throwable) {
        }
        return false
    }
}