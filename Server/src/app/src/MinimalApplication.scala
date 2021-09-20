package app

import scalatags.Text.all._
import scalatags.generic
import scalatags.text.Builder

object MinimalApplication extends cask.MainRoutes {
    val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

    @cask.staticResources("/static")
    def staticResources() = "static"

    var openConnection = Set.empty[cask.WsChannelActor]

    var messages: Seq[(String, String)] = Vector(
        ("alice", "Hello World!"),
        ("bob", "I am cow, hear me moo"),
        ("urmat", "Test row")
    )

    @cask.websocket("/subscribe")
    def subscribe() = cask.WsHandler { connection =>
        connection.send(cask.Ws.Text(messageList().render))
        openConnection += connection
        cask.WsActor { case cask.Ws.Close(_, _) => openConnection -= connection }
    }

    @cask.get("/")
    def hello(): doctype = doctype("html")(
        html(
            head(
                link(rel := "stylesheet", href := bootstrap),
                script(src := "/static/app.js")
            ),
            body(
                div(cls := "container")(
                    h1("Scala chat!"),
                    div(
                        div(id := "messageList")(messageList()),
                    ),
                    div(id := "errorDiv", color.red),
                    form(onsubmit := "submitForm(); return false;", method := "post")(
                        input(`type` := "text", id := "nameInput", placeholder := "User name"),
                        input(`type` := "text", id := "msgInput", placeholder := "Write message!"),
                        input(`type` := "submit")
                    )
                )
            )
        )
    )

    @cask.postJson("/")
    def postChatMsg(name: String, msg: String): ujson.Obj = {
        if (name == "") {
            ujson.Obj("success" -> false, "err" -> "Name can not be empty")
        } else if (msg == "")
            ujson.Obj("success" -> false, "err" -> "Message can not be empty")
        else {
            messages = messages :+ (name -> msg)
            for (connection <- openConnection) connection.send(cask.Ws.Text(messageList().render))
            ujson.Obj("success" -> true, "err" -> "")
        }
    }

    def messageList(): generic.Frag[Builder, String] = frag(for ((name, message) <- messages) yield p(b(name), " ", message))

    initialize()
}
