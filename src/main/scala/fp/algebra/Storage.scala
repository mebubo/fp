package fp.algebra

import fp.{Binary, Episode}

trait Storage[F[_]] {
  def write(e: Episode, data: Binary): F[Unit]
}
