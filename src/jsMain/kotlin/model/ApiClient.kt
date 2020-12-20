package model

import com.github.kyamada.sample.model.TaskData
import com.github.kyamada.sample.model.TasksResponse
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window

object ApiClient {

    private val baseUrl: String
        get() {
            val origin = window.location.origin
            return if (origin.contains("localhost")) {
                "http://localhost:8080"
            } else {
                origin
            }
        }

    private val jsonClient = HttpClient {
        install(JsonFeature) { serializer = KotlinxSerializer() }
    }

    suspend fun getTasks(page: Int, perPage: Int, userId: Int? = null): TasksResponse {
        val query = "page=$page&per_page=$perPage"
        return jsonClient.get("$baseUrl/v1/tasks?$query")
    }

    suspend fun addTask(taskData: TaskData): TaskData {
        return jsonClient.post("$baseUrl/v1/tasks") {
            contentType(ContentType.Application.Json)
            body = taskData
        }
    }

    suspend fun deleteTask(id: Int) {
        jsonClient.delete<Unit>("$baseUrl/v1/tasks/$id")
    }
}
