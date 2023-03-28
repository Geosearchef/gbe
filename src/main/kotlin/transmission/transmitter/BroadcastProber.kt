package transmission.transmitter

import BROADCAST_MULTICAST_ADDRESS
import BROADCAST_PORT
import transmission.protocol.BroadcastProtocol
import transmission.protocol.BroadcastProtocolMessage
import java.net.DatagramPacket
import java.net.DatagramSocket

object BroadcastProber {

    fun probe() {
        val datagramSocket = DatagramSocket()

        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        val requestPacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)
        BroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), peerAddress = BROADCAST_MULTICAST_ADDRESS, peerPort = BROADCAST_PORT)
            .toDatagramPacket(requestPacket)

        println("Probing from 0.0.0.0:${datagramSocket.localPort}...")
        datagramSocket.send(requestPacket)

    }


}