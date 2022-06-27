package com.globodai.ovestiaire.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.globodai.ovestiaire.data.network.Resource
import com.google.android.material.snackbar.Snackbar


fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT ).show()
}

fun Fragment.toast(message: String){
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT ).show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}


fun View.snackbarLengthIndefinite(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Veuillez vérifier votre accès au réseau",
            retry
        )
        /*
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("You've entered incorrect email or password")
            } else {
                (this as BaseFragment<*, *, *>).logout()
            }
        }
        */
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbarLengthIndefinite(error)
        }
    }
}