package me.brunofelix.googlecertapp.extensions

import android.widget.EditText
import me.brunofelix.googlecertapp.utils.MaskUtil

fun EditText.myCustomMask(mask: String) {
    addTextChangedListener(MaskUtil(this, mask))
}