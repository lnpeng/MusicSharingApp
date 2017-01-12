trait SongRepo {
  def getLength(songId: Int): Future[Option[Int]] //get the length of a song in seconds. This is an async method. Returns None if the song is not found.
}
