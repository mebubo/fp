package fp.impl

import cats.effect.IO
import fp.{Podcast, URL}
import fp.algebra.Config

import scala.io.Source

object FileReader {
  def readFile(path: String): IO[List[String]] = IO {
     Source.fromFile(path).getLines.toList
  }
}

object ConfigReader {
  def configToPodcast(l: String): Podcast = {
    val name :: url :: _ = l.split(" ").toList
    Podcast(name, URL(url))
  }
}

object ConfigIO extends Config[IO] {
  override def read: IO[List[Podcast]] =
    FileReader.readFile("podcasts.conf").map(x => x.map(ConfigReader.configToPodcast))
}
