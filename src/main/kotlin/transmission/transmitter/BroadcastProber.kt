package transmission.transmitter

import java.net.DatagramPacket
import java.net.DatagramSocket

object BroadcastProber {

    fun probe() {
        val datagramSocket = DatagramSocket()

        val requestBuffer = ByteArray(64000)
        val responseBuffer = ByteArray(64000)

        val probePacket = DatagramPacket(requestBuffer, 0, requestBuffer.size)

    }


}