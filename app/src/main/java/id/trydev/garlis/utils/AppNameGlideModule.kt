package id.trydev.garlis.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

@GlideModule
class AppNameGlideModule : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply {
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(ObjectKey(System.currentTimeMillis().toShort())) }
    }

}