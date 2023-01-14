package transmission.protocol

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import util.getSystemIdentifier
import java.nio.ByteBuffer


val PROTOCOL_GSON = Gson()



data class PeerInfo(
    @SerializedName("ver") val version: Byte,
    @SerializedName("id") val identifier: String,
    @SerializedName("sys") val system: String = getSystemIdentifier()
): BroadcastProtocolMessage()

open class BroadcastProtocolMessage {
    fun applyToBuffer(buffer: ByteBuffer) {
        val messageString = PROTOCOL_GSON.toJson(this)
        val messageBytes = messageString.toByteArray()

        buffer.putInt(messageBytes.size)
        buffer.put(messageBytes)
    }

    companion object {
        fun <T> getMessageFromBuffer(type: Class<T>, buffer: ByteBuffer): T {
            val length = buffer.int
            val messageBytes = ByteArray(length)
            buffer.get(messageBytes, 0, length) // 0 is the offset into the array, the buffer starts at its current index
            val messageString = String(messageBytes)
            val message = PROTOCOL_GSON.fromJson<T>(messageString, type)

            return message
        }
    }
}
