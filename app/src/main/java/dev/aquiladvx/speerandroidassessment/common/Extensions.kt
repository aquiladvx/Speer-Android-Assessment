package dev.aquiladvx.speerandroidassessment.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dev.aquiladvx.speerandroidassessment.R
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors


fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}

fun <T> fromJson(json: String?, mClass: Class<T>): T {
    return Gson().fromJson(json ?: "", mClass)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun EditText.hideKeyboard() {
    clearFocus()
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.showErrorMessage(error: GithubNetworkErrors) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error_dialog_title))
    builder.setMessage(getString(error.userMessageId ?: R.string.unknown_error_message))
    builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
        dialog.dismiss()
    }
    builder.show()
}

fun Context.loadImage(url: String, imageView: ImageView) {
    Glide
        .with(this)
        .load(url)
        .into(imageView)
}