package com.kigya.unique.ui.tabs.sheet.teacher

import com.kigya.unique.utils.extensions.string.findMatchInOrNull

object TeacherValidator {
    operator fun invoke(
        name: String,
        controlList: List<String>,
        successBlock: () -> Unit,
        errorBlock: () -> Unit,
    ): String? {
        if (name.length >= 3) {
            val match = name.findMatchInOrNull(controlList)
            if (!match.isNullOrBlank()) {
                successBlock()
                return match
            } else {
                errorBlock()
            }
        }
        return null
    }
}
