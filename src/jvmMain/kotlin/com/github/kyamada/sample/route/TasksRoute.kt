package com.github.kyamada.sample.route

import com.github.kyamada.sample.database.DatabaseFactory.dbQuery
import com.github.kyamada.sample.database.Task
import com.github.kyamada.sample.database.Tasks
import com.github.kyamada.sample.model.PageInfo
import com.github.kyamada.sample.model.TaskData
import com.github.kyamada.sample.model.TasksResponse
import com.github.kyamada.sample.model.exception.BadRequestException
import com.github.kyamada.sample.model.exception.NotFoundException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll

fun Route.tasks() {
    route("/tasks") {
        get("/") {
            val pageInfo = PageInfo.fromQueryParameters(call.request.queryParameters)
            var totalCount: Long = 0
            val tasks = dbQuery {
                val query = Tasks.selectAll()
                totalCount = query.count()
                query.orderBy(Tasks.id to SortOrder.DESC)
                    .limit(pageInfo.perPage, offset = pageInfo.offset)
                    .toList().map {
                        Task.wrapRow(it).toData()
                    }
            }
            val response = TasksResponse(tasks, totalCount)
            call.respond(response)
        }

        post("/") {
            val newTask = call.receive<TaskData>()
            val task = dbQuery {
                Task.new {
                    title = newTask.title
                }.toData()
            }
            call.respond(task)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw BadRequestException()
            dbQuery {
                val task = Task.findById(id) ?: throw NotFoundException()
                task.delete()
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
