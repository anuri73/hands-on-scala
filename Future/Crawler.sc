import $file.FetchLinks
import FetchLinks._

import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

def fetchAllLinks(startTitle: String, depth: Int): Set[String] = {

    var seen = Set(startTitle)
    var current = Set(startTitle)
    for (i <- Range(0, depth)) {
        //        val nextTitleLists = for (title <- current) yield fetchLinks(title)
        val futures = for (title <- current) yield Future {
            fetchLinks(title)
        }
        val nextTitleLists = futures.map(Await.result(_, Inf))
        current = nextTitleLists.flatten.filter(!seen.contains(_))
        seen = seen ++ current
    }
    seen
}

println(fetchAllLinks("Singapore", 2).mkString("\n"))
