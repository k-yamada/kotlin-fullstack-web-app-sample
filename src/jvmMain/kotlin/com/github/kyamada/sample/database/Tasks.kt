package com.github.kyamada.sample.database

import com.github.kyamada.sample.model.TaskData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

object Tasks : IntIdTable("tasks") {
    val title = varchar("title", 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
}

class Task(id: EntityID<Int>) : IntEntity(id) {
    var title by Tasks.title
    var createdAt by Tasks.createdAt
    var updatedAt by Tasks.updatedAt

    fun toData(): TaskData {
        return TaskData(
            id = id.value,
            title = title,
            createdAt = createdAt.millis,
            updatedAt = updatedAt.millis
        )
    }

    companion object : IntEntityClass<Task>(Tasks)
}
