package fp.algebra

import fp.{Episode, Podcast}

trait DB[F[_]] {
  def createTables: F[Unit]
  def getPodcasts: F[List[Podcast]]
  def getEpisodes(p: Podcast): F[List[Episode]]
  def addPodcast(p: Podcast): F[Podcast]
  def addEpisode(e: Episode): F[Episode]
  def markDone(e: Episode): F[Episode]
}
