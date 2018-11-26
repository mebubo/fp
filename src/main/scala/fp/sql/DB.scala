package fp.sql

import cats.effect.IO
import cats.syntax.applicative._
import doobie.implicits._
import doobie.hi.{FDMD, HC, HRS}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import fp.sql.Constants.Tables.{Episodes, Podcasts}
import fp.model._

object DB {

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

  def addPodcast(p: Podcast): ConnectionIO[DBPodcast] = for {
    _ <- sql"insert or ignore into podcasts (name, url) values (${p.name}, ${p.url})".update.run
    p <- sql"select id, name, url from podcasts where name = ${p.name}".query[DBPodcast].unique
  } yield p

  def addEpisode(e: Episode[Id]): ConnectionIO[DBEpisode[Id]] = for {
    _ <- sql"insert or ignore into episodes (podcastid, url, done) values (${e.podcast}, ${e.url}, ${e.done}".update.run
    e <- sql"select id, podcastid, name, url from episodes where url = ${e.url}".query[DBEpisode[Id]].unique
  } yield e

  def getPodcasts: ConnectionIO[List[DBPodcast]] =
    sql"""select * from podcasts""".query[DBPodcast].to[List]

  def getEpisodes(p: DBPodcast): ConnectionIO[List[DBEpisode[Id]]] =
    sql"""select * from episodes where podcastid = ${p.id}""".query[DBEpisode[Id]].to[List]


  def markDone(e: DBEpisode[Id]): ConnectionIO[DBEpisode[Id]] = for {
    _ <- sql"update episodes set done = true where id = ${e.id}".update.run
    id <- sql"select lastval()".query[Long].unique
    e <- sql"select * from episodes where id = ${id}".query[DBEpisode[Id]].unique
  } yield e

}
