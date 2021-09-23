import scala.concurrent._
import java.util.concurrent.Executors

implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))


def futureFunc(): Future[String] = {
    Future {
        "Chinatown.jpg"
    }
}

def callbackFunc(onSuccess: String => Unit, onError: Throwable => Unit): Unit = {
    futureFunc().onComplete {
        case scala.util.Success(str) => onSuccess(str)
        case scala.util.Failure(ex) => onError(ex)
    }
}
callbackFunc(println, println)
