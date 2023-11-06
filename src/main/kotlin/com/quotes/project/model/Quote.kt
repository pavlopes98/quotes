package com.quotes.project.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "quotes")
data class Quote (
    @Id
    val _id: String,
    val quoteText: String,
    val quoteAuthor: String,
    val quoteGenre: String,
    val __v: Int
)
