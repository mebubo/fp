package fp.impl

import cats.effect.Sync
import cats.implicits._
import fp.algebra.Config
import fp.model.{Podcast, URL}

import scala.io.Source

object FileReader {
  def readFile[F[_]: Sync](path: String): F[List[String]] = Sync[F].delay {
    Source.fromFile(path).getLines.toList
  }
}

object ConfigReader {
  def configToPodcast(l: String): Podcast = {
    val name :: url :: _ = l.split(" ").toList
    Podcast(name, URL(url))
  }
}

class FileConfig[F[_]: Sync] extends Config[F] {
  override def read: F[List[Podcast]] = {
    val value: F[List[String]] = FileReader.readFile("podcasts.conf")
    value.map(x => x.map(ConfigReader.configToPodcast))
  }
}
