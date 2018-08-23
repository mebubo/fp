package fp.logic

import cats._
import cats.implicits._
import fp.{DBPodcast, Episode, Podcast}
import fp.algebra.{Config, DB, HTTP, Storage}

class DownloadPodcasts[F[_]: Monad](C: Config[F], D: DB[F]/*, H: HTTP[F], S: Storage[F]*/) {
  def init: F[Unit] = {
    D.createTables
  }

  def newPodcastsInConfig: F[List[Podcast]] = {
    for {
      pc <- C.read
      pdb <- D.getPodcasts
    } yield pc.filterNot(pdb.map(_.toPodcast).toSet)
  }

  def addNewPodcasts: F[List[DBPodcast]] = {
    for {
      np <- newPodcastsInConfig
      ps <- np.traverse(D.addPodcast)
    } yield ps
  }

//  def newEpisodes(p: Podcast): F[List[Episode]] = {
//    for {
//      eh <- H.getEpisodes(p)
//      edb <- D.getEpisodes(p)
//    } yield eh.filterNot(edb.toSet)
//  }
//
//  def addNewEpisodes: F[Episode] = {
//    for {
//      ps <- D.getPodcasts
//      ne <- ps.traverse(newEpisodes)
//      _ <- ne.flatten.traverse(D.addEpisode)
//    } yield ()
//  }

}
