package com.javiermp.openbankmobiletest.common.errorhandling

enum class AppAction constructor(val code: Long) {
    UNKNOWN(-1L),
    GET_CHARACTERS(2L),
    GET_CHARACTER_DETAIL(3L);

    override fun toString(): String {
        return this.code.toString()
    }

    companion object {

        fun fromCode(code: Long): AppAction {
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
