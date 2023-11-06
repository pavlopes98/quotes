package com.quotes.project.repository

import com.quotes.project.model.Quote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface QuoteRepository: MongoRepository <Quote, String>
