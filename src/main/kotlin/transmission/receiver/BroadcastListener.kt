package transmission.receiver

import BROADCAST_MULTICAST_ADDRESS
import BROADCAST_PORT
import GBEMain
import transmission.protocol.BroadcastProtocol
import transmission.protocol.BroadcastProtocol.CMD_ESTABLISH_CONNECTION
import transmission.protocol.BroadcastProtocol.CMD_PROBE
import transmission.protocol.BroadcastProtocolMessage
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.MulticastSocket

object BroadcastListener {

    private val multicastSocket = MulticastSocket(null)

    fun init() {
        multicastSocket.reuseAddress = true
        multicastSocket.bind(InetSocketAddress(BROADCAST_PORT))

        multicastSocket.joinGroup(InetAddress.getByName(BROADCAST_MULTICAST_ADDRESS)) // the non deprecated method wants an interface, but there's no way to get the default, this method does that automatically

        println("Broadcast listener socket bound to $BROADCAST_PORT, starting listener...")
        Thread(::broadcastThread).start()
    }


    private fun broadcastThread() {
        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        while(GBEMain.running) {
            val requestPacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)
            multicastSocket.receive(requestPacket)

            if(requestPacket.length < 2) {
                // invalid
                continue
            }

            // parse the received packet
            val requestMessage = BroadcastProtocolMessage.getMessageFromPacket(requestPacket)

            if(requestMessage.peerInfo.guid == GBEMain.guid) {
                println("Received own broadcast, skipping")
                continue  // skip our own packet
            }

            // create a response
            val responsePacket = DatagramPacket(responseBuffer, 0, responseBuffer.size)

            when(requestMessage.cmd) {
                CMD_PROBE -> {
                    println("Received broadcast probe from ${requestMessage.peerAddress}:${requestMessage.peerPort}, version ${requestMessage.peerVersion}, identifier: ${requestMessage.peerInfo.identifier}, system: ${requestMessage.peerInfo.system}, guid: ${requestMessage.peerInfo.guid}")

                    BroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeReplyData(), requestMessage = requestMessage)
                        .toDatagramPacket(responsePacket)
                }
                CMD_ESTABLISH_CONNECTION -> {
                    throw NotImplementedError() // TODO
                }
                else -> {
                    continue
                }
            }

            multicastSocket.send(responsePacket)
        }
    }

}

