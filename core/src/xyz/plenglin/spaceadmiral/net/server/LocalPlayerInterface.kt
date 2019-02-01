package xyz.plenglin.spaceadmiral.net.server

import xyz.plenglin.spaceadmiral.net.client.LocalServerInterface

class LocalPlayerInterface : PlayerInterface {
    lateinit var clientSide: LocalServerInterface
    override val connected: Boolean = true
}

fun generateEntangledPair(): Pair<LocalPlayerInterface, LocalServerInterface> {
    val pi = LocalPlayerInterface()
    val si = LocalServerInterface(pi)
    return pi to si
}
