package com.javiermp.openbankmobiletest.common.widget.error

import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle

interface ErrorListener {

    fun onRetry(errorBundle: ErrorBundle)
}