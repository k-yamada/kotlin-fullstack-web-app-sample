package com.github.kyamada.sample

import com.github.kyamada.sample.database.DatabaseFactory
import com.github.kyamada.sample.model.Env
import com.github.kyamada.sample.model.exception.InternalServerErrorException
import com.github.kyamada.sample.model.exception.SystemException
import com.github.kyamada.sample.route.tasks
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.html.*
import mu.KotlinLogging
import kotlin.collections.set

private val logger = KotlinLogging.logger {}

fun HTML.index() {
    head {
        title("TODO")
        meta {
            charset = "UTF-8"
        }
        meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1"
        }
        link(href = "https://v5.getbootstrap.jp/docs/5.0/examples/dashboard/", rel = "canonical")

        // Bootstrap core CSS
        link(
            href = "https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/css/bootstrap.min.css",
            rel = "stylesheet"
        ) {
            attributes["crossorigin"] = "anonymous"
            integrity = "sha384-r4NyP46KrjDleawBgD5tp8Y7UzmLA05oM1iAEQ17CSuDqnUK2+k9luXQOfXJCJ4I"
        }
    }
    body {
        div {
            id = "root"
        }
        script(src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js") {
            attributes["crossorigin"] = "anonymous"
            integrity = "sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        }
        script(src = "https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha1/js/bootstrap.min.js") {
            attributes["crossorigin"] = "anonymous"
            integrity = "sha384-oesi62hOLfzrys4LxRF63OJCXdXDipiYWBnvTl9Y9/TRlw5xlKIEHpNyvvDShgf/"
        }
        script(src = "/static/output.js") {}
    }
}

fun main() {
    DatabaseFactory.init()
    io.ktor.server.netty.EngineMain.main(emptyArray())
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        when (Env.shortEnv) {
            // local環境では異なるPORTでJSサーバを起動するため、JSサーバからのアクセスを許可する.
            "local" -> anyHost()
            else -> {
                // TODO: ドメイン が決まったらドメインを指定する
                anyHost()
            }
        }
    }
    install(Compression) {
        gzip()
    }
    install(StatusPages) {
        exception<SystemException> { cause ->
            if (cause is InternalServerErrorException) {
                logger.error("SystemException", cause)
            }
            call.response.status(cause.status)
            call.respond(cause.response())
        }
    }
    routing {
        route("/v1") {
            get("/health") {
                call.respond(HttpStatusCode.OK)
            }
            tasks()
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}
