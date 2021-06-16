package com.javiermp.openbankmobiletest.characters.viewmodel

import com.javiermp.model.exception.HTTPException
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.errorhandling.AppAction
import com.javiermp.openbankmobiletest.common.errorhandling.AppError
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundleBuilder
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Factory used to create error bundles from an Exception as source.
 */
class CharactersErrorBuilder : ErrorBundleBuilder {

    override fun build(throwable: Throwable, appAction: AppAction): ErrorBundle {
        val message = throwable.message?.let {
            if (it.isNotEmpty()) it
            else "There was an application error"
        } ?: "There was an application error"

        // Unwrap RuntimeExceptions, wrapped by emitter.tryOnError()
        val t = if (throwable is RuntimeException && throwable !is HTTPException && throwable.cause != null && throwable.cause is Exception) throwable.cause as Exception else throwable

        val stringId = when (t) {
            is UnknownHostException -> R.string.error_no_connection
            is TimeoutException -> R.string.error_connection_timeout
            is NoRouteToHostException, is ConnectException -> R.string.error_cannot_connect_to_server
            is HTTPException -> {
                when (t.statusCode) {
                    401 -> R.string.error_unauthorized
                    403 -> R.string.error_forbidden
                    404 -> R.string.error_not_found
                    405 -> R.string.error_method_not_allowed
                    409 -> R.string.error_conflict
                    else -> R.string.error_default_message
                }
            }
            else -> R.string.error_default_message
        }

        val appError = when (t) {
            is UnknownHostException -> AppError.NO_INTERNET
            is TimeoutException -> AppError.TIMEOUT
            is NoRouteToHostException, is ConnectException -> AppError.NO_ROUTE_TO_HOST
            is HTTPException -> {
                when (t.statusCode) {
                    401 -> AppError.UNAUTHORIZED
                    403 -> AppError.FORBIDDEN
                    404 -> AppError.NOT_FOUND
                    405 -> AppError.NOT_ALLOWED
                    409 -> AppError.CONFLICT
                    else -> AppError.GENERAL_ERROR
                }
            }
            else -> AppError.GENERAL_ERROR
        }

        return ErrorBundle(t, stringId, message, appAction, appError)
    }
}
