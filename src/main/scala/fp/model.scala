package fp

object model {
  final case class URL(value: String) extends AnyVal
  final case class Podcast(name: String, url: URL)
  final case class Episode[A](podcast: A, url: URL, done: Boolean)
  final case class Binary(data: Array[Byte])
  type Id = Int

  final case class DBPodcast(id: Id, podcast: Podcast)
  final case class DBEpisode[A](id: Id, episode: Episode[A])

}
