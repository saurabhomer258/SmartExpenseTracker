package com.example.smartexpensetracker.nav

sealed class Route(val path: String) {
    data object Entry : Route("entry")
    data object List : Route("list")
    data object Report : Route("report")
}