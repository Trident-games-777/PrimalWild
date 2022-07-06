package com.bigfishgames.fairwaysolitaireuniversalf2pgoogl.wild_repository

import android.content.Context
import android.util.Log

class WildRepository(
    private val context: Context,
    private val base: String
) {
    private val fileName = "url_file"

    fun save(file: String) {
        if (!file.contains(base) && file != load()) {
            Log.d("YYY", "Save data to file: $file")
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(file.toByteArray(charset = Charsets.UTF_8))
            }
        }
    }

    fun load(): String {
        try {
            context.openFileInput(fileName).bufferedReader(charset = Charsets.UTF_8).useLines {
                return it.first()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            return base
        }
    }
}