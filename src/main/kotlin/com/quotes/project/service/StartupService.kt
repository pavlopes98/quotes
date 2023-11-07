package com.quotes.project.service

import com.quotes.project.repository.QuoteRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class StartupService (
    private val quoteRepository: QuoteRepository,
    private val quoteService: QuoteService
) {

    @PostConstruct
    fun startupCheck() {
        val maxItems = 50000L
        if (quoteRepository.count() != maxItems) {
            quoteService.cleanData()
            quoteService.loadDataFromURL(maxItems)
        }
    }
}
