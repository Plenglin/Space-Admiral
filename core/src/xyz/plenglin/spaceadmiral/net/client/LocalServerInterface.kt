package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.net.server.LocalPlayerInterface

class LocalServerInterface(val serverSide: LocalPlayerInterface) : ServerInterface {
    override val connected: Boolean = true

}