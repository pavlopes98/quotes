package com.quotes.project.controller

import com.quotes.project.model.Quote
import com.quotes.project.service.QuoteService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import java.util.*

@SpringBootTest
internal class QuoteControllerTest {
    @MockBean
    private lateinit var quoteService: QuoteService

    private lateinit var quoteController: QuoteController

    private val quote1 = Quote(
        _id = "1",
        quoteText = "abc",
        quoteAuthor = "xyz",
        quoteGenre = "lmnop",
        __v = 0
    )

    private val quote2 = Quote(
        _id = "2",
        quoteText = "xyz",
        quoteAuthor = "abc",
        quoteGenre = "lmnop",
        __v = 0
    )

    @BeforeEach
    fun setup() {
        quoteController = QuoteController(quoteService)
    }

    @Test
    fun `getQuote should return successfully a quote when an id that exists is provided`() {
        `when`(quoteService.getQuoteById(quote1._id)).thenReturn(Optional.of(quote1))

        val response = quoteController.getQuoteById(id = quote1._id)

        assertEquals(response.statusCode, HttpStatus.OK)

        val responseBody = response.body

        assertEquals(responseBody?.get()?._id, quote1._id)
    }

    @Test
    fun `getQuote should return not found when an id that does not exist is provided`() {
        `when`(quoteService.getQuoteById(quote1._id)).thenReturn(Optional.empty())

        val response = quoteController.getQuoteById(id = "abc")

        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `getQuote should return successfully a quote when an author that exists is provided`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteService.getQuoteByAuthor(quote1.quoteAuthor, pageRequest)).thenReturn(PageImpl(listOf(quote1)))

        val response = quoteController.getQuotesByAuthor(author = quote1.quoteAuthor, pageRequest)

        assertEquals(response.statusCode, HttpStatus.OK)

        val responseBody = response.body

        assertEquals(responseBody?.content?.size, 1)
        assertEquals(responseBody?.content?.get(0)?.quoteAuthor, quote1.quoteAuthor)
    }

    @Test
    fun `getQuote should return not found when an author that does not exist is provided`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteService.getQuoteByAuthor("abc", pageRequest)).thenReturn(PageImpl(emptyList()))

        val response = quoteController.getQuotesByAuthor(author = "abc", pageRequest)

        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `getQuote should return successfully all items`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteService.getAllQuotes(pageRequest)).thenReturn(PageImpl(listOf(quote1, quote2)))

        val response = quoteController.getAllQuotes(pageRequest)

        assertEquals(response.statusCode, HttpStatus.OK)

        val responseBody = response.body

        assertEquals(responseBody?.size, 2)
        assertEquals(responseBody?.content, listOf(quote1, quote2))
    }

    @Test
    fun `getQuote should return not found when no items in data collection`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteService.getAllQuotes(pageRequest)).thenReturn(PageImpl(emptyList()))

        val response = quoteController.getAllQuotes(pageRequest)

        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }
}
