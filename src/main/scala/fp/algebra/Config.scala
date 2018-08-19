package fp.algebra

import fp.Podcast

trait Config[F[_]] {
  def read: F[List[Podcast]]
}
