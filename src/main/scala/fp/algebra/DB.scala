package fp.algebra

import fp.model._

trait DB[F[_]] {
  def createTables: F[Unit]
  def getPodcasts: F[List[DBPodcast]]
  def getEpisodes(p: DBPodcast): F[List[DBEpisode[Id]]]
  def addPodcast(p: Podcast): F[DBPodcast]
  def addEpisode(e: Episode[Id]): F[DBEpisode[Id]]
  def markDone(e: DBEpisode[Id]): F[DBEpisode[Id]]
}
