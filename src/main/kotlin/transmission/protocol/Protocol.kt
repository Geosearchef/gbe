package transmission.protocol

import APPLICATION_VERSION
import GBEMain
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import util.getSystemIdentifier
import java.net.DatagramPacket
import java.net.InetAddress
import java.nio.ByteBuffer


val PROTOCOL_GSON = Gson()

object BroadcastProtocol {
    const val CMD_PROBE: Byte = 30
    const val CMD_ESTABLISH_CONNECTION: Byte = 31

    const val CMD_PROBE_REPLY: Byte = 40

    val dataTypeByCmd = mapOf(
        CMD_PROBE to BroadcastProbeData::class.java,
        CMD_PROBE_REPLY to BroadcastProbeReplyData::class.java,
        CMD_ESTABLISH_CONNECTION to BroadcastConnectData::class.java
    )
    fun getTypeByCmd(cmd: Byte) = dataTypeByCmd[cmd]
    fun <T> getCmdByType(type: Class<T>): Byte? = dataTypeByCmd.filterValues { it == type }.entries.firstOrNull()?.key

    // payloads
    open class BroadcastData
    class BroadcastProbeData : BroadcastData()
    class BroadcastProbeReplyData : BroadcastData()
    class BroadcastConnectData : BroadcastData()
}



//data class PeerInfo(
//    @SerializedName("ver") val version: Byte,
//    @SerializedName("id") val identifier: String,
//    @SerializedName("sys") val system: String = getSystemIdentifier(),
//    override val cmd: Byte = -1,
//    override val peerVersion: Byte = -1
//): BroadcastProtocolMessage(cmd, peerVersion)


// Peer Info is part of the protocol
// It is therefore set on sending (converting) a message
//

class ReceivedBroadcastProtocolMessage(
    data: BroadcastProtocol.BroadcastData,
    val peerInfo: PeerInfo,
    val sourceAddress: String,
    val sourcePort: Int,
    val peerVersion: Byte
) : BroadcastProtocolMessage(data)

class SendingBroadcastProtocolMessage(
    data: BroadcastProtocol.BroadcastData,
    val targetAddress: String,
    val targetPort: Int,
) : BroadcastProtocolMessage(data) {

    constructor(
        data: BroadcastProtocol.BroadcastData,
        requestMessage: ReceivedBroadcastProtocolMessage,
    ) : this(data, targetAddress = requestMessage.sourceAddress, targetPort = requestMessage.sourcePort)

    fun toDatagramPacket(packet: DatagramPacket): DatagramPacket {
        val buffer = ByteBuffer.wrap(packet.data).apply {
            put(APPLICATION_VERSION)
            put(cmd)
            putObj(PeerInfo(APPLICATION_VERSION, GBEMain.guid, GBEMain.identifier))
            putObj(data)
        }

        packet.address = InetAddress.getByName(targetAddress)
        packet.port = targetPort
        packet.length = buffer.position()

        return packet
    }

}


open class BroadcastProtocolMessage(
    val data: BroadcastProtocol.BroadcastData,
) {

    val cmd: Byte = BroadcastProtocol.getCmdByType(data::class.java) ?: throw RuntimeException("Invalid type, no command found for type: ${data::class.java.canonicalName}")


    companion object {
        fun getMessageFromPacket(packet: DatagramPacket): ReceivedBroadcastProtocolMessage {
            val senderAddress = packet.address.hostAddress
            val senderPort = packet.port

            val buffer = ByteBuffer.wrap(packet.data, 0, packet.length)

            val version = buffer.get()
            val cmd = buffer.get()
            val peerInfo = buffer.getObj(PeerInfo::class.java)
            val dataType = BroadcastProtocol.getTypeByCmd(cmd)
                ?: throw RuntimeException("Invalid cmd $cmd from $senderAddress:$senderPort")

            val dataObj = buffer.getObj(dataType)

            val message = ReceivedBroadcastProtocolMessage(dataObj, peerInfo, senderAddress, senderPort, version)

            return message
        }

        fun <T> ByteBuffer.getObj(type: Class<T>) = getMessageObjectFromBuffer(type, this)
        fun <T> getMessageObjectFromBuffer(type: Class<T>, buffer: ByteBuffer): T {
            val length = buffer.int
            val objectBytes = ByteArray(length)
            buffer.get(objectBytes, 0, length) // 0 is the offset into the array, the buffer starts at its current index
            val objectString = String(objectBytes)
            val obj = PROTOCOL_GSON.fromJson(objectString, type)

            return obj
        }

        fun <T> ByteBuffer.putObj(obj: T) = applyMessageObjectToBuffer(obj, this)
        fun <T> applyMessageObjectToBuffer(obj: T, buffer: ByteBuffer) {
            val objectString = PROTOCOL_GSON.toJson(obj)
            val objectBytes = objectString.toByteArray()

            buffer.putInt(objectBytes.size)
            buffer.put(objectBytes)
        }
    }

    data class PeerInfo(
        @SerializedName("ver") val version: Byte,
        @SerializedName("guid") val guid: String,
        @SerializedName("id") val identifier: String,
        @SerializedName("sys") val system: String = getSystemIdentifier(),
    )
}
