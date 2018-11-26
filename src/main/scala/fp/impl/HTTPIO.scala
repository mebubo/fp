package fp.impl

import cats.effect.IO
import fp.model.{Binary, Episode, Podcast}
import fp.algebra.HTTP

object HTTPIO extends HTTP[IO] {
  override def getEpisodes(podcast: Podcast): IO[List[Episode[Podcast]]] = ???

  override def downloadEpisode(e: Episode[Podcast]): IO[Binary] = ???
}
