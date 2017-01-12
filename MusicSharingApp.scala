package interview.iheartmedia

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import akka.actor.{ActorRef, ActorSystem, Props}

class MusicSharingApp(songRepo: SongRepo) {
  val musicAppSystem = ActorSystem("MusicSharingApp")
  val musicRooms: ConcurrentMap[Int, ActorRef] = new ConcurrentHashMap

  def joinOrCreateRoom(roomId: Int, channel: Channel): Unit = {
    musicRooms.putIfAbsent(roomId, musicAppSystem.actorOf(Props(new MusicRoom(roomId, channel, songRepo)), name = s"room${roomId}"))
    musicRooms.get(roomId) ! JoinRoom
  }
}
