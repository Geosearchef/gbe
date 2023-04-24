package transmission.protocol

import java.net.DatagramPacket
import kotlin.test.Test
import kotlin.test.assertEquals

object ProtocolTest {
    @Test
    fun validateProbePacket() {
        val buffer = ByteArray(64000)
        val packet = DatagramPacket(buffer, 0, buffer.size)

        BroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), peerAddress = "1.2.3.4", peerPort = 54321)
            .toDatagramPacket(packet)

        assertEquals("1.2.3.4", packet.address.hostAddress)
        assertEquals(54321, packet.port)
    }

    @Test
    fun testProbePacket() {
        val buffer = ByteArray(64000)
        val packet = DatagramPacket(buffer, 0, buffer.size)

        BroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), peerAddress = "1.2.3.4", peerPort = 54321)
            .toDatagramPacket(packet)

        // TODO: deserialize again
    }
}