package utils

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.util.*


class Util {
    companion object {

        final val TAG = "LoggerInfo"

        @Throws(IOException::class)
        fun getProperty(key: String?, context: Context): String {
            val properties = Properties()
            val assetManager: AssetManager = context.assets
            val inputStream: InputStream = assetManager.open("logincredential.properties")
            properties.load(inputStream)
            return properties.getProperty(key)
        }
    }
}