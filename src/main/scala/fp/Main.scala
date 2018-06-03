package fp

import cats.effect.IO

object Main {

  val tables: IO[List[String]] = DB.transact(DB.tables)

  val createTables: IO[Unit] = DB.transact(DB.create)

  def addPodcast(p: Podcast): IO[Podcast] = DB.transact(DB.addPodcast(p))

  def mainIO(args: Array[String]): IO[List[String]] = for {
    _ <- createTables
    tables <- tables
    _ <- IO { println(tables) }
    p <- addPodcast(Podcast("1", URL("1")))
    _ <- IO { println(p) }
  } yield List()

  def main(args: Array[String]): Unit = {
    mainIO(args).unsafeRunSync
  }

}
