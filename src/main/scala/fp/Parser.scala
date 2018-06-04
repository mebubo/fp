package fp

import scala.io.Source
import scala.util.Try
import scala.xml.{NodeSeq, XML}
import cats.implicits._

case class Item(title: String, enclosureUrl: URL)
case class Feed(title: String, items: List[Item])

object Parser {

  type E[A] = Either[String, A]

  def item2episode(p: Podcast, i: Item): Episode =
    Episode(0, p, i.enclosureUrl, done = false)

  def parse(s: String): E[Feed] = for {
    el <- Try(XML.loadString(s)).toEither.left.map(e => e.getMessage)
    channel =  el \\ "rss" \\ "channel"
    t <- title(channel)
    i <- items(channel)
  } yield Feed(t, i)

  def title(channel: NodeSeq): E[String] = {
    (channel \\ "title").map(_.text).headOption.toRight("No channel title")
  }

  def items(channel: NodeSeq): E[List[Item]] = {
    val items = channel \\ "item"
    val is: List[E[Item]] = items.map { i =>
      val title: E[String] = (i \\ "title").headOption.toRight("No episode title").map(_.text)
      val url: E[URL] = (i \\ "enclosure").headOption.flatMap(x => x.attributes.asAttrMap.get("url")).map(URL).toRight("No episode url")
      for {
        t <- title
        u <- url
      } yield Item(t, u)
    }.toList
    is.sequence[E, Item]
  }

  def main(args: Array[String]): Unit = {
    val xml = Source.fromFile("feed.xml").mkString
    println(parse(xml))
  }
}
