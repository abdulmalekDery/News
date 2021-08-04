package com.beinmedia.test.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter

object BindingUtils {
    @BindingAdapter("error")
    @JvmStatic
    fun bindError(view: EditText, error: String) {
        view.error = error
    }

//    @BindingAdapter("errord")
//    @JvmStatic
//    fun bindError(view: MaskedEditText, error: String) {
//        view.error = error
//    }
}

//@BindingAdapter("error")
//fun bindError(view: EditText, error: String?) {
//    view.error = error
//}
//
//@BindingAdapter("error")
//fun bindError(view: MaskedEditText, error: String?) {
//    view.error = error
//}