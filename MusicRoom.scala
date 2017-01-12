package interview.iheartmedia

import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.Actor

import scala.concurrent.{Await, Future}

class MusicRoom(id: Int, channel: Channel, songRepo: SongRepo) extends Actor {
  val songVotes: ConcurrentSkipListMap[Int, Int] = new ConcurrentSkipListMap
  val users: AtomicInteger = new AtomicInteger(0)

  channel.onReceive({
    case AddSong(id) =>
      self ! AddSong(id)
    case VoteToSkipSong(id) =>
      self ! VoteToSkipSong(id)
  })

  playSongList

  def waitTillSongFinished(id: Int): Unit = {
    val len = Await.result(songRepo.getLength(id), 1 second)
    if (len != Some(0) && len != None) {
      Thread.sleep(1000)
      waitTillSongFinished(id)
    }
  }

  def playSongList: Unit = Future {
    println(s"start to play song list: ${songVotes.size} songs")
    songVotes.entrySet().toArray.map(
      songVote => songVote match {
        case e: java.util.Map.Entry[Int, Int] =>
          if (e.getValue <= users.get / 2) {
            println(s"playing song ${e.getKey}")
            channel.pushSong(e.getKey)
            waitTillSongFinished(e.getKey)
          }
        case _ =>
      }
    )
    Thread.sleep(1000)
    playSongList
  }

  def receive = {
    case AddSong(songId) =>
      println(s"Add song $songId")
      songVotes.putIfAbsent(songId, 0)
    case VoteToSkipSong(songId) =>
      println(s"Vote to skip song $songId")
      songVotes.replace(songId, songVotes.get(songId) + 1)
    case JoinRoom =>
      println(s"Join room ${id}")
      users.incrementAndGet()
    case _ =>
      println(s"room ${id} received a message")
  }
}
