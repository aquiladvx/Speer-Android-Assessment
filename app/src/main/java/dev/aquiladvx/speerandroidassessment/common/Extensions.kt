package dev.aquiladvx.speerandroidassessment.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.gson.Gson

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}

fun <T> fromJson(json: String?, mClass: Class<T>): T {
    return Gson().fromJson(json?: "", mClass)
}