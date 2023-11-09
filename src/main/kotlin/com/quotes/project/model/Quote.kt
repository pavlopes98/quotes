package com.quotes.project.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed

@Document(collection = "quotes")
data class Quote (
    @Id
    val _id: String,
    val quoteText: String,
    @Indexed
    val quoteAuthor: String,
    val quoteGenre: String,
    val __v: Int
)
