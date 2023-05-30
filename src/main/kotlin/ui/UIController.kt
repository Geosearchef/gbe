package ui

import transmission.protocol.BroadcastProtocolMessage

interface UIController {

    fun init()

    fun updateReceivers(receivers: List<BroadcastProtocolMessage.PeerInfo>)

}