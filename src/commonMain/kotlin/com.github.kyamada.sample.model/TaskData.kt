package com.github.kyamada.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskData(
    val id: Int,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)
