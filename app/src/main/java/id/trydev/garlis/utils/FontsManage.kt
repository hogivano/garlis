package id.trydev.garlis.utils

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import com.google.gson.TypeAdapterFactory
import java.util.*

class FontsManage constructor(context: Context) {
    public fun robotoRegular(context: Context) : Typeface{
        var am: AssetManager = context.applicationContext.assets
        var typeface: Typeface = Typeface.createFromAsset(am, String.format(Locale.US,
            "fonts/%s", "roboto_regular.ttf"))
        return typeface
    }
}