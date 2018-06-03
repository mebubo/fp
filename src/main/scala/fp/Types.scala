package fp

case class URL(value: String)

case class Podcast(name: String, url: URL)

case class Episode(id: Int, podcast: Podcast, url: URL, done: Boolean)
