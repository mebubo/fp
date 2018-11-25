package fp.algebra

import fp.model.{Binary, Episode}

trait Storage[F[_]] {
  def write(e: Episode, data: Binary): F[Unit]
}
