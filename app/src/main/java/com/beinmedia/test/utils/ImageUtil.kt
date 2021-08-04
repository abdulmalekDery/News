package com.beinmedia.test.utils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.beinmedia.test.R
import com.beinmedia.test.dependencyinjection.module.GlideApp


fun ImageView.loadImage(url: String?, placeholderAndError: Int = -1) = url?.isNotEmpty().let {
    if (it == true) {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)


        Glide.with(this.context)
            .load(url)
            .apply(options)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this)
    }
}

fun ImageView.loadImageDialog(url: String?, placeholderAndError: Int = -1) = url?.isNotEmpty().let {
    if (it == true) {
        val options = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)


        Glide.with(this.context)
            .load(url)
            .apply(options)
            .into(this)
    }
}

fun ImageView.loadLocalImage(url: String?, placeholderAndError: Int = -1) = url?.isNotEmpty().let {
    if (it == true) {
        val options = RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH)

        Glide.with(this.context)
                .load(url)
                .apply(options)
                .into(this)
    }
}
fun ImageView.loadResourceImage(url: Int, placeholderAndError: Int = -1) = url.let {
    val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)

    if (placeholderAndError != -1) {
        options.placeholder(placeholderAndError)
        options.error(placeholderAndError)
    }


    Glide.with(this.context)
            .load(url)
            .apply(options)
            .into(this)
}

fun ImageView.loadCircleImage(url: String?, placeholderAndError: Int = -1) = url?.isNotEmpty().let {
    if (it == true) {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.HIGH)



        GlideApp.with(this.context)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }
}