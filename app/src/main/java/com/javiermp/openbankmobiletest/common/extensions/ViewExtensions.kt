package com.javiermp.openbankmobiletest.common.extensions

import android.view.View
import com.javiermp.openbankmobiletest.common.view.OnSingleClickListener

fun View.setOnSingleClickListener(listener: (View) -> Unit) {
    this.setOnClickListener(
        OnSingleClickListener.wrap(View.OnClickListener(listener))
    )
}