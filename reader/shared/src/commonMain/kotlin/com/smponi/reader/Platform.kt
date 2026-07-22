package com.smponi.reader

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
