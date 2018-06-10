package fp

final case class URL(value: String) extends AnyVal

final case class Podcast(name: String, url: URL)

final case class Episode(id: Int, podcast: Podcast, url: URL, done: Boolean)
