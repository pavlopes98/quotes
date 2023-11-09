package com.quotes.project.controller

import com.quotes.project.model.Quote
import com.quotes.project.service.QuoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/api/quotes")
class QuoteController (@Autowired private val quoteService: QuoteService){

    @GetMapping("/{id}")
    fun getQuoteById(@PathVariable("id") id: String): ResponseEntity<Optional<Quote>> {
        val quote = quoteService.getQuoteById(id)
        return if (quote.isEmpty) ResponseEntity(HttpStatus.NOT_FOUND)
        else ResponseEntity(quote, HttpStatus.OK)
    }
}
