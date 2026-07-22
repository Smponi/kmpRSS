package com.smponi.reader

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = sayHello(platform.name)
}
