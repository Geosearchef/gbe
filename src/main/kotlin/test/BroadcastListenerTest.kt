package test

import transmission.receiver.BroadcastListener
import transmission.transmitter.BroadcastProber

fun main() {
    println("Starting broadcast listener test")
    BroadcastListener.init()

    Thread.sleep(1000)
    BroadcastProber.probe()
}