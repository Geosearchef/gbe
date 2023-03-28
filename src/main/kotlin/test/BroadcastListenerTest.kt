package test

import transmission.receiver.BroadcastListener
import transmission.transmitter.BroadcastProber


fun main(args: Array<String>) {
    println("listening...")
    BroadcastListener.init()

    Thread.sleep(1000)
    BroadcastProber.probe()
}