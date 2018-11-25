package fp.algebra

import fp.model.Podcast

trait Config[F[_]] {
  def read: F[List[Podcast]]
}
