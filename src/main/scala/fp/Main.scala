package fp

import cats.implicits._
import cats.effect.IO
import fp.impl.{ConfigIO, DBIO}
import fp.logic.DownloadPodcasts

object Main {

  def mainIO(args: Array[String])(DP: DownloadPodcasts[IO]): IO[List[DBPodcast]] = for {
    _ <- DP.init
    ps <- DP.addNewPodcasts
    _ <- IO { println(ps) }
  } yield ps

  def main(args: Array[String]): Unit = {
    val dp = new DownloadPodcasts[IO](ConfigIO, DBIO)
    mainIO(args)(dp).unsafeRunSync
  }

}
