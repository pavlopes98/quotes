package com.quotes.project.load

import com.quotes.project.model.Quote
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class QuoteApiLoadTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `api should support 50 concurrent requests`() {
        val numThreads = 50
        val id = "5eb17aadb69dc744b4e70d45"
        val uri = UriComponentsBuilder.fromUriString("/api/quotes/$id")
            .build()
            .toUri()

        runBlocking {
            val threads = List(numThreads) {
                launch {
                    val response = restTemplate.getForEntity(uri, Quote::class.java)
                    assertEquals(response.statusCode, HttpStatus.OK)
                }
            }

            threads.forEach { it.join() }
        }
    }
}
