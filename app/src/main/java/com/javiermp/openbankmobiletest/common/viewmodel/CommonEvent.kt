package com.javiermp.openbankmobiletest.common.viewmodel

sealed class CommonEvent {

    object Unauthorized : CommonEvent()
}