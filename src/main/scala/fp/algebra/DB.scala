package fp.algebra

import fp.{DBEpisode, DBPodcast, Episode, Podcast}

trait DB[F[_]] {
  def createTables: F[Unit]
  def getPodcasts: F[List[DBPodcast]]
  def getEpisodes(p: DBPodcast): F[List[DBEpisode]]
  def addPodcast(p: Podcast): F[DBPodcast]
  def addEpisode(e: Episode): F[DBEpisode]
  def markDone(e: DBEpisode): F[DBEpisode]
}
