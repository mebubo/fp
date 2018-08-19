package fp.impl

import cats.effect.IO
import fp.{Episode, Podcast}
import fp.algebra.DB
import fp.db.DB

object DBIO extends DB[IO] {
  override def createTables: IO[Unit] = DB.transact(DB.createTables)

  override def getPodcasts: IO[List[Podcast]] = DB.transact(DB.getPodcasts)

  override def getEpisodes(p: Podcast): IO[List[Episode]] = DB.transact(DB.getEpisodes(p))

  override def addPodcast(p: Podcast): IO[Podcast] = DB.transact(DB.addPodcast(p))

  override def addEpisode(e: Episode): IO[Episode] = DB.transact(DB.addEpisode(e))

  override def markDone(e: Episode): IO[Episode] = ???
}
