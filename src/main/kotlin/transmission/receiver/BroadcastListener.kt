package transmission.receiver

import BROADCAST_PORT
import GBEMain
import transmission.protocol.BroadcastProtocol
import transmission.protocol.BroadcastProtocol.CMD_ESTABLISH_CONNECTION
import transmission.protocol.BroadcastProtocol.CMD_PROBE
import transmission.protocol.BroadcastProtocolMessage
import java.net.DatagramPacket
import java.net.DatagramSocket

object BroadcastListener {

    private val datagramSocket: DatagramSocket

    init {
        datagramSocket = DatagramSocket(BROADCAST_PORT)

        Thread(::broadcastThread).start()
    }


    private fun broadcastThread() {
        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        while(GBEMain.running) {
            val requestPacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)
            datagramSocket.receive(requestPacket)

            if(requestPacket.length < 2) {
                // invalid
                continue
            }

            // parse the received packet
            val requestMessage = BroadcastProtocolMessage.getMessageFromPacket(requestPacket)

            // create a response
            val responsePacket = DatagramPacket(responseBuffer, 0, responseBuffer.size)

            when(requestMessage.cmd) {
                CMD_PROBE -> {
                    println("Received broadcast probe from ${requestMessage.peerAddress}:${requestMessage.peerPort}, version ${requestMessage.peerVersion}, identifier: ${requestMessage.peerInfo.identifier}, system: ${requestMessage.peerInfo.system}")

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

            datagramSocket.send(responsePacket)
        }
    }

}

