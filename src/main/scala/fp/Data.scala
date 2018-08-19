package fp

final case class URL(value: String) extends AnyVal

final case class Podcast(id: Int, name: String, url: URL)

final case class Episode(id: Int, podcastId: Int, url: URL, done: Boolean)

final case class Binary(data: Array[Byte])
