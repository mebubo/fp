package fp.impl

import cats.effect.IO
import fp.model.{Binary, Episode, Podcast}
import fp.algebra.HTTP

object HTTPIO extends HTTP[IO] {
  override def getEpisodes(podcast: Podcast): IO[List[Episode]] = ???

  override def downloadEpisode(e: Episode): IO[Binary] = ???
}
