package com.quotes.project.controller

import com.quotes.project.configuration.CustomPageImpl
import com.quotes.project.model.Quote
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class QuoteControllerIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `endpoint response time is less than 200 milliseconds when id is provided`() {
        val id = "5eb17aadb69dc744b4e70d31"
        val uri = UriComponentsBuilder.fromUriString("/api/quotes/$id")
            .build()
            .toUri()

        val startTime = System.currentTimeMillis()
        restTemplate.getForEntity(uri, Quote::class.java)
        val endTime = System.currentTimeMillis()

        val responseTime = Duration.ofMillis(endTime - startTime)
        val maxResponseTime = Duration.ofMillis(200)

        assert(responseTime < maxResponseTime)
    }

    @Test
    fun `endpoint response time is less than 200 milliseconds when author is provided`() {
        val author = "Pablo Picasso"
        val uri = UriComponentsBuilder.fromUriString("/api/quotes/author/$author")
            .build()
            .toUri()
        val responseType = object : ParameterizedTypeReference<CustomPageImpl<Quote>>() {}

        val startTime = System.currentTimeMillis()
        restTemplate.exchange(uri, HttpMethod.GET, null, responseType)
        val endTime = System.currentTimeMillis()

        val responseTime = Duration.ofMillis(endTime - startTime)
        val maxResponseTime = Duration.ofMillis(200)

        assert(responseTime < maxResponseTime)
    }

    @Test
    fun `endpoint response time is less than 30 seconds when retrieving all items`() {
        val uri = UriComponentsBuilder.fromUriString("/api/quotes")
            .build()
            .toUri()
        val responseType = object : ParameterizedTypeReference<CustomPageImpl<Quote>>() {}

        val startTime = System.currentTimeMillis()
        restTemplate.exchange(uri, HttpMethod.GET, null, responseType)
        val endTime = System.currentTimeMillis()

        val responseTime = Duration.ofMillis(endTime - startTime)
        val maxResponseTime = Duration.ofMillis(30000)

        assert(responseTime < maxResponseTime)
    }
}
