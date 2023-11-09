package com.quotes.project.service


import com.quotes.project.model.Pagination
import com.quotes.project.model.Quote
import com.quotes.project.model.QuoteApiResponse
import com.quotes.project.repository.QuoteRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.web.client.RestTemplate
import java.util.*

@ActiveProfiles("test")
@SpringJUnitConfig
@SpringBootTest
internal class QuoteServiceTest {
    @MockBean
    private lateinit var quoteRepository: QuoteRepository

    @MockBean
    private lateinit var restTemplate: RestTemplate

    private lateinit var quoteService: QuoteService

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
        quoteService = QuoteService(quoteRepository, restTemplate)
    }

    @Test
    fun `should clean all data`() {
        quoteService.cleanData()
        verify(quoteRepository, times(1)).deleteAll()
    }

    @Test
    fun `should save data if response status code is 200`() {
        val maxItems = 1L
        val url = "https://quote-garden.onrender.com/api/v3/quotes?limit=$maxItems"

        val mockResponse = QuoteApiResponse(
            statusCode = 200,
            message = "abcde",
            pagination = Pagination(currentPage = 1, nextPage = 2, totalPages = 3),
            totalQuotes = 10,
            data = listOf(quote1)
        )

        `when`(restTemplate.getForObject(url, QuoteApiResponse::class.java))
            .thenReturn(mockResponse)

        quoteService.loadDataFromURL(maxItems)

        verify(quoteRepository).save(mockResponse.data[0])
    }

    @Test
    fun `should handle error when response status code is not 200`() {
        val maxItems = 1L
        val url = "https://quote-garden.onrender.com/api/v3/quotes?limit=$maxItems"

        val mockResponse = QuoteApiResponse(
            statusCode = 500,
            message = "abcde",
            pagination = Pagination(currentPage = 1, nextPage = 2, totalPages = 3),
            totalQuotes = 1,
            data = listOf()
        )

        `when`(restTemplate.getForObject(url, QuoteApiResponse::class.java))
            .thenReturn(mockResponse)

        assertThrows<Error> { quoteService.loadDataFromURL(maxItems) }
    }

    @Test
    fun `getQuoteById should return a quote when it exists`() {
        `when`(quoteRepository.findById(quote1._id)).thenReturn(Optional.of(quote1))

        val response = quoteService.getQuoteById(quote1._id)

        assertThat(response.isPresent).isTrue
        assertThat(response.get()).isEqualTo(quote1)
    }

    @Test
    fun `getQuoteById should return an empty Optional when it does not exist`() {
        val quoteId = "quoteId"

        `when`(quoteRepository.findById(quoteId)).thenReturn(Optional.empty())

        val response = quoteService.getQuoteById(quoteId)

        assertThat(response.isEmpty).isTrue
    }

    @Test
    fun `getQuoteByAuthor should return a quote when it exists`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteRepository.findByQuoteAuthor(quote1.quoteAuthor, pageRequest)).thenReturn(PageImpl(listOf(quote1)))

        val response = quoteService.getQuoteByAuthor(quote1.quoteAuthor, pageRequest)

        assertThat(response.content).isEqualTo(listOf(quote1))
    }

    @Test
    fun `getQuoteByAuthor should return an empty Optional when it does not exist`() {
        val pageRequest = PageRequest.of(0, 10)
        val quoteAuthor = "quoteAuthor1"

        `when`(quoteRepository.findByQuoteAuthor(quoteAuthor, pageRequest)).thenReturn(PageImpl(emptyList()))

        val response = quoteService.getQuoteByAuthor(quoteAuthor, pageRequest)

        assertThat(response.content.isEmpty()).isTrue
    }

    @Test
    fun `getAllQuotes should return all quotes when it exists`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteRepository.findAll(pageRequest)).thenReturn(PageImpl(listOf(quote1, quote2)))

        val response = quoteService.getAllQuotes(pageRequest)

        assertThat(response.isEmpty).isFalse
        assertThat(response.content).isEqualTo(listOf(quote1, quote2))
    }

    @Test
    fun `getAllQuotes should return an empty list when it does not exist`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(quoteRepository.findAll(pageRequest)).thenReturn(PageImpl(emptyList()))

        val response = quoteService.getAllQuotes(pageRequest)

        assertThat(response.isEmpty).isTrue
    }
}
