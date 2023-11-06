package com.quotes.project.model

data class QuoteApiResponse (
    val statusCode: Int,
    val message: String,
    val pagination: Pagination,
    val totalQuotes: Int,
    val data: List<Quote>
)

data class Pagination(
    val currentPage: Int,
    val nextPage: Int,
    val totalPages: Int
)
