package com.javiermp.openbankmobiletest.common.errorhandling

enum class AppError constructor(val code: Long) {
    UNKNOWN(-1L),
    GENERAL_ERROR(1L),
    NO_INTERNET(2L),
    TIMEOUT(3L),
    NO_ROUTE_TO_HOST(4L),
    UNAUTHORIZED(401L),
    FORBIDDEN(403L),
    NOT_FOUND(404L),
    NOT_ALLOWED(405L),
    CONFLICT(409L);

    override fun toString(): String {
        return this.code.toString()
    }

    companion object {

        fun fromCode(code: Long): AppError {
            var result = UNKNOWN

            val retryActions = values()
            var i = 0
            while (i < retryActions.size && result == UNKNOWN) {
                val retryAction = retryActions[i]
                if (retryAction.code == code) {
                    result = retryAction
                }
                ++i
            }

            return result
        }
    }
}
