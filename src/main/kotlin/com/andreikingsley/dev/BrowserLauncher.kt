package com.andreikingsley.dev

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(
    name = ["app.open-browser"],
    havingValue = "true",
)
class BrowserLauncher(
    private val environment: Environment,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun openBrowser() {
        val port = environment.getProperty("server.port") ?: "8080"
        val url = "http://localhost:$port/home"

        when {
            System.getProperty("os.name").startsWith("Windows") ->
                ProcessBuilder("cmd", "/c", "start", url).start()

            System.getProperty("os.name").startsWith("Mac") ->
                ProcessBuilder("open", url).start()

            else ->
                ProcessBuilder("xdg-open", url).start()
        }
    }
}