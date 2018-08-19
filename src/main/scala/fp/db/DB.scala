package fp.db

import cats.effect.IO
import cats.syntax.applicative._
import doobie.implicits._
import doobie.hi.{FDMD, HC, HRS}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import Constants.Tables.{Episodes, Podcasts}
import fp.{Episode, Podcast}

object DB {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO]("org.sqlite.JDBC", "jdbc:sqlite:./fp.db")

  def getTables: ConnectionIO[List[String]] = HC.getMetaData(
    FDMD.getTables("", "", "", null)
      .flatMap(rs => FDMD.embed(rs, HRS.list[(Option[String], Option[String], String, Option[String])])))
      .map(xs => xs.map(_._3))

  def createPodcastsTable: ConnectionIO[Int] =
    sql"""
         CREATE TABLE podcasts (
                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       name TEXT NOT NULL UNIQUE,
                       url TEXT NOT NULL)
       """.update.run


  def createEpisodesTable: ConnectionIO[Int] =
    sql"""
          CREATE TABLE episodes (
                       id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       podcastid INTEGER NOT NULL,
                       url TEXT NOT NULL,
                       done INTEGER NOT NULL,
                       UNIQUE(podcastid, url),
                       UNIQUE(podcastid, id))
       """.update.run

  def createTables: ConnectionIO[Unit] = for {
    tables <- getTables
    _ <- createPodcastsTable.unlessA(tables.contains(Podcasts))
    _ <- createEpisodesTable.unlessA(tables.contains(Episodes))
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

  def addEpisode(e: Episode): ConnectionIO[Episode] = for {
    _ <- sql"insert into episodes (podcastid, url, done) values (${e.podcastId}, ${e.url}, ${e.done}".update.run
    id <- sql"select last_insert_rowid()".query[Long].unique
    p2 <- sql"select id, podcastid, name, url from episodes where id = $id".query[Episode].unique
  } yield p2

  def getPodcasts: ConnectionIO[List[Podcast]] =
    sql"""select * from podcasts""".query[Podcast].to[List]

  def getEpisodes(p: Podcast): ConnectionIO[List[Episode]] =
    sql"""select * from episodes where podcastid = ${p.id}""".query[Episode].to[List]

}
