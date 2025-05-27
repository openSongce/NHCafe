package com.ssafy.nhcafe.ui.common


fun String.camelToSnake(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
}
