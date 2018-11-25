package fp

import cats.Monad
import cats.implicits._
import cats.effect.{ContextShift, IO, Sync}
import doobie.util.transactor.Transactor
import fp.impl.{DB, FileConfig}
import fp.logic.DownloadPodcasts
import fp.model.DBPodcast
import scala.concurrent.ExecutionContext

object Main {

  def run[F[_] : Monad : Sync](args: Array[String])(DP: DownloadPodcasts[F]): F[List[DBPodcast]] = for {
    _ <- DP.init
    ps <- DP.addNewPodcasts
    _ <- Sync[F].delay { println(ps) }
  } yield ps

  def main(args: Array[String]): Unit = {
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    implicit val xa: Transactor[IO] = Transactor.fromDriverManager[IO]("org.sqlite.JDBC", "jdbc:sqlite:./fp.db")
    val dp = new DownloadPodcasts[IO](new FileConfig[IO], new DB[IO])
    run(args)(dp).unsafeRunSync
  }

}
