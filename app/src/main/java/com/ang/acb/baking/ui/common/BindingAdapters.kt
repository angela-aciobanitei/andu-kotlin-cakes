package com.ang.acb.baking.ui.common

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * A [BindingAdapter] that toggles a [View]'s visibility between visible and gone.
 */
@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}