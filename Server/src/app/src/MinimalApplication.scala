package app

import scalatags.Text.all._
import scalatags.generic
import scalatags.text.Builder

object MinimalApplication extends cask.MainRoutes {
    val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

    @cask.staticResources("/static")
    def staticResources() = "static"

    var openConnection = Set.empty[cask.WsChannelActor]

    case class Message(name: String, message: String)

    import com.opentable.db.postgres.embedded.EmbeddedPostgres

    val server: EmbeddedPostgres = EmbeddedPostgres.builder()
        .setDataDirectory((os.pwd / "data").toString())
        .setCleanDataDirectory(false)
        .setPort(5432)
        .start()

    import io.getquill._
    import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

    val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
    pgDataSource.setUser("postgres")

    val hikariConfig: HikariConfig = new HikariConfig()
    hikariConfig.setDataSource(pgDataSource)

    val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
    ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, message text);")

    import ctx._

    def messages: List[(String, String)] = ctx.run(query[Message].map(m => (m.name, m.message)))

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
    def postChatMsg(name: String, message: String): ujson.Obj = {
        if (name == "") {
            ujson.Obj("success" -> false, "err" -> "Name can not be empty")
        } else if (message == "")
            ujson.Obj("success" -> false, "err" -> "Message can not be empty")
        else {
            ctx.run(query[Message].insert(lift(Message(name, message))))
            for (connection <- openConnection) connection.send(cask.Ws.Text(messageList().render))
            ujson.Obj("success" -> true, "err" -> "")
        }
    }


    def messageList(): generic.Frag[Builder, String] = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))

    initialize()
}
