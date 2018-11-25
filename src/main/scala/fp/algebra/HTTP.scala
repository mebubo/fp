package fp.algebra

import fp.model.{Binary, Episode, Podcast}

trait HTTP[F[_]] {
  def getEpisodes(podcast: Podcast): F[List[Episode]]
  def downloadEpisode(e: Episode): F[Binary]
}
