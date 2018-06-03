package fp

import cats.effect.IO
import cats.syntax.applicative._
import doobie.implicits._
import doobie.hi.{FDMD, HC, HRS}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import Constants.Tables.{Podcasts, Episodes}

object DB {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO]("org.sqlite.JDBC", "jdbc:sqlite:./fp.db")

  def tables: ConnectionIO[List[String]] = HC.getMetaData(
    FDMD.getTables("", "", "", null)
      .flatMap(rs => FDMD.embed(rs, HRS.list[(Option[String], Option[String], String, Option[String])])))
      .map(xs => xs.map(_._3))

  def createPodcasts: ConnectionIO[Int] =
    sql"""
         CREATE TABLE podcasts (
                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       name TEXT NOT NULL UNIQUE,
                       url TEXT NOT NULL)
       """.update.run


  def createEpisodes: ConnectionIO[Int] =
    sql"""
          CREATE TABLE episodes (
                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       podcastid INTEGER NOT NULL,
                       url TEXT NOT NULL,
                       done INTEGER NOT NULL,
                       UNIQUE(podcastid, url),
                       UNIQUE(podcastid, id))
       """.update.run

  def create: ConnectionIO[Unit] = for {
    tables <- tables
    _ <- createPodcasts.unlessA(tables.contains(Podcasts))
    _ <- createEpisodes.unlessA(tables.contains(Episodes))
  } yield ()

  def transact[A](c: ConnectionIO[A]): IO[A] = c.transact(xa)

  def addPodcast0(p: Podcast): ConnectionIO[Podcast] =
    sql"insert into podcasts (name, url) values (${p.name}, ${p.url.value})"
      .update
      .withUniqueGeneratedKeys("id", "name", "url")

  def addPodcast(p: Podcast): ConnectionIO[Podcast] = for {
    _ <- sql"insert into podcasts (name, url) values (${p.name}, ${p.url})".update.run
    id <- sql"select last_insert_rowid()".query[Long].unique
    p2 <- sql"select id, name, url from podcasts where id = $id".query[Podcast].unique
  } yield p2

}
