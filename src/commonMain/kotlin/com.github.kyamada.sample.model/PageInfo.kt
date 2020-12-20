package com.github.kyamada.sample.model

import io.ktor.http.*

data class PageInfo(val page: Int, val perPage: Int) {
    val offset: Long get() = ((page - 1) * perPage).toLong()

    companion object {
        private const val DEFAULT_PER_PAGE = 10

        fun fromQueryParameters(queryParameters: Parameters): PageInfo {
            val page = queryParameters["page"]?.toInt() ?: 1
            val perPage = queryParameters["per_page"]?.toInt() ?: DEFAULT_PER_PAGE
            return PageInfo(page, perPage)
        }
    }
}
