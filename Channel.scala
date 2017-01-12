trait Channel {
  def pushSong(songId: Int): Unit

  // push a song to the client which will start to play
  def onReceive(handler: (Message) => Unit) // incoming messages from the client will be passed to the handler
}
