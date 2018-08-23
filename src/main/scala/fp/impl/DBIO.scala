package fp.impl

import cats.effect.IO
import fp.{DBEpisode, DBPodcast, Episode, Podcast}
import fp.algebra.DB
import fp.db.DB

object DBIO extends DB[IO] {
  override def createTables: IO[Unit] = DB.transact(DB.createTables)

  override def getPodcasts: IO[List[DBPodcast]] = DB.transact(DB.getPodcasts)

  override def getEpisodes(p: DBPodcast): IO[List[DBEpisode]] = DB.transact(DB.getEpisodes(p))

  override def addPodcast(p: Podcast): IO[DBPodcast] = DB.transact(DB.addPodcast(p))

  override def addEpisode(e: Episode): IO[DBEpisode] = DB.transact(DB.addEpisode(e))

  override def markDone(e: DBEpisode): IO[DBEpisode] = DB.transact(DB.markDone(e))
}
