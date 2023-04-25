package transmission.transmitter

import BROADCAST_MULTICAST_ADDRESS
import BROADCAST_PORT
import mu.KotlinLogging
import transmission.protocol.BroadcastProtocol
import transmission.protocol.SendingBroadcastProtocolMessage
import java.net.DatagramPacket
import java.net.DatagramSocket

object BroadcastProber {

    private val logger = KotlinLogging.logger {  }

    fun probe() {
        val datagramSocket = DatagramSocket()

        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        val requestPacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)
        SendingBroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), targetAddress = BROADCAST_MULTICAST_ADDRESS, targetPort = BROADCAST_PORT)
            .toDatagramPacket(requestPacket)

        logger.debug { "Probing from 0.0.0.0:${datagramSocket.localPort}..." }
        datagramSocket.send(requestPacket)

        // TODO: listen for a few seconds on same socket (port) and call callback if receiver found
    }


}