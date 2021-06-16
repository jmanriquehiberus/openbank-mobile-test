package com.javiermp.openbankmobiletest.common.permissions

class Permissions {
    companion object {
        private const val PERMISSION_ACCESS_FINE_LOCATION = 1
        val permissionsCode: Map<String, Int> = mapOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION to PERMISSION_ACCESS_FINE_LOCATION
        )
    }
}