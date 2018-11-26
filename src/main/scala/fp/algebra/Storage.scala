package fp.algebra

import fp.model.{Binary, Episode, Podcast}

trait Storage[F[_]] {
  def write(e: Episode[Podcast], data: Binary): F[Unit]
}
