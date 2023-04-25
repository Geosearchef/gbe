package transmission.protocol

import APPLICATION_VERSION
import GBEMain
import util.getSystemIdentifier
import java.net.DatagramPacket
import kotlin.test.Test
import kotlin.test.assertEquals

object ProtocolTest {
    @Test
    fun validateProbePacket() {
        val buffer = ByteArray(64000)
        val packet = DatagramPacket(buffer, 0, buffer.size)

        SendingBroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), targetAddress = "1.2.3.4", targetPort = 54321)
            .toDatagramPacket(packet)

        assertEquals("1.2.3.4", packet.address.hostAddress)
        assertEquals(54321, packet.port)
    }

    @Test
    fun testProbePacket() {
        val buffer = ByteArray(64000)
        val packet = DatagramPacket(buffer, 0, buffer.size)

        SendingBroadcastProtocolMessage(BroadcastProtocol.BroadcastProbeData(), targetAddress = "1.2.3.4", targetPort = 54321)
            .toDatagramPacket(packet)

        // deserialize
        val message = BroadcastProtocolMessage.getMessageFromPacket(packet)

        assertEquals(BroadcastProtocol.CMD_PROBE, message.cmd)
        assertEquals(BroadcastProtocol.BroadcastProbeData::class.java, message.data::class.java)
        assertEquals(APPLICATION_VERSION, message.peerVersion)
        assertEquals(APPLICATION_VERSION, message.peerInfo.version)
        assertEquals(GBEMain.guid, message.peerInfo.guid)
        assertEquals(getSystemIdentifier(), message.peerInfo.system)
        assertEquals(GBEMain.identifier, message.peerInfo.identifier)
    }
}