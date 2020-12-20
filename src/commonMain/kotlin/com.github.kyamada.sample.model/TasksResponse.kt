package com.github.kyamada.sample.model

import kotlinx.serialization.Serializable

@Serializable
class TasksResponse(
        val tasks: List<TaskData>,
        val totalCount: Long
)
