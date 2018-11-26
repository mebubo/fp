package fp.algebra

import fp.model.{Binary, Episode, Podcast}

trait HTTP[F[_]] {
  def getEpisodes(podcast: Podcast): F[List[Episode[Podcast]]]
  def downloadEpisode(e: Episode[Podcast]): F[Binary]
}
