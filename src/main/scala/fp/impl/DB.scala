package fp.impl

import cats.Monad
import doobie.util.transactor.Transactor
import fp.model.{DBEpisode, DBPodcast, Episode, Podcast}
import fp.algebra
import fp.sql
import doobie.implicits._

class DB[F[_]: Monad: Transactor] extends algebra.DB[F] {

  val T: Transactor[F] = implicitly[Transactor[F]]

  override def createTables: F[Unit] = sql.DB.createTables.transact(T)

  override def getPodcasts: F[List[DBPodcast]] = sql.DB.getPodcasts.transact(T)

  override def getEpisodes(p: DBPodcast): F[List[DBEpisode]] = sql.DB.getEpisodes(p).transact(T)

  override def addPodcast(p: Podcast): F[DBPodcast] = sql.DB.addPodcast(p).transact(T)

  override def addEpisode(e: Episode): F[DBEpisode] = sql.DB.addEpisode(e).transact(T)

  override def markDone(e: DBEpisode): F[DBEpisode] = sql.DB.markDone(e).transact(T)
}
