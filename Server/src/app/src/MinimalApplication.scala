package app

import scalatags.Text.all._

object MinimalApplication extends cask.MainRoutes {
    val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

    var messages: Seq[(String, String)] = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))

    @cask.get("/")
    def hello(): doctype = doctype("html")(
        html(
            head(
                link(rel := "stylesheet", href := bootstrap)
            ),
            body(
                div(cls := "container")(
                    h1("Scala chat!"),
                    div(
                        for ((name, msg) <- messages) yield p(b(name), " ", msg)
                    ),
                    div(
                        input(`type` := "text", placeholder := "User name"),
                        input(`type` := "text", placeholder := "Write message!"),
                    )
                )
            )
        )
    )

    @cask.post("/do-thing")
    def doThing(request: cask.Request): String = {
        request.text().reverse
    }

    initialize()
}
