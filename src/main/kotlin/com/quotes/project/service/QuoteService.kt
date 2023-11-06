package com.quotes.project.service

import com.quotes.project.model.QuoteApiResponse
import com.quotes.project.repository.QuoteRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class QuoteService (private val quoteRepository: QuoteRepository, private val restTemplate: RestTemplate) {

    fun cleanData() = quoteRepository.deleteAll()

    fun loadDataFromURL( maxItems: Long) {

        val url = "https://quote-garden.onrender.com/api/v3/quotes?limit=$maxItems"
        val response = restTemplate.getForObject(url, QuoteApiResponse::class.java)

        when (response?.statusCode) {
            200 -> {
                response.data.forEach {
                    quoteRepository.save(it)
                }
            }
            else -> throw Error("Error: Status Code ${response?.statusCode} - Could not load data from the URL")
        }
    }
}
