package fp

final case class URL(value: String) extends AnyVal

final case class DBPodcast(id: Int, name: String, url: URL) {
  def toPodcast: Podcast = Podcast(name, url)
}

final case class Podcast(name: String, url: URL)

final case class DBEpisode(id: Int, podcastId: Int, url: URL, done: Boolean) {
  def toEpisode: Episode = Episode(podcastId, url, done)
}

final case class Episode(podcastId: Int, url: URL, done: Boolean)

final case class Binary(data: Array[Byte])
