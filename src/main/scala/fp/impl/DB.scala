package fp.impl

import cats.Monad
import doobie.util.transactor.Transactor
import fp.model._
import fp.algebra
import fp.sql
import doobie.implicits._

class DB[F[_]: Monad: Transactor] extends algebra.DB[F] {

  val T: Transactor[F] = implicitly[Transactor[F]]

  override def createTables: F[Unit] = sql.DB.createTables.transact(T)

  override def getPodcasts: F[List[DBPodcast]] = sql.DB.getPodcasts.transact(T)

  override def getEpisodes(p: DBPodcast): F[List[DBEpisode[Id]]] = sql.DB.getEpisodes(p).transact(T)

  override def addPodcast(p: Podcast): F[DBPodcast] = sql.DB.addPodcast(p).transact(T)

  override def addEpisode(e: Episode[Id]): F[DBEpisode[Id]] = sql.DB.addEpisode(e).transact(T)

  override def markDone(e: DBEpisode[Id]): F[DBEpisode[Id]] = sql.DB.markDone(e).transact(T)
}
