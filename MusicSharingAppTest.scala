package interview.iheartmedia

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.Future

class MusicSharingAppTest extends FunSuite with MockitoSugar {
  test("Music sharing app") {
    val songRepo = mock[SongRepo]
    when(songRepo.getLength(any[Int])) thenReturn Future {
      Some(0)
    }
    val channel = mock[Channel]
    val app = new MusicSharingApp(songRepo)
    app.joinOrCreateRoom(1, channel)
    val room1 = app.musicAppSystem.actorSelection("/user/room1")
    room1 ! AddSong(3)
    room1 ! AddSong(4)
    room1 ! AddSong(5)
    room1 ! VoteToSkipSong(5)
    Thread.sleep(1000000)
  }
}
