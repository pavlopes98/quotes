package com.quotes.project.repository

import com.quotes.project.model.Quote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QuoteRepository: MongoRepository <Quote, String>{

    fun findByQuoteAuthor(quoteAuthor: String) : Optional<List<Quote>>
}
