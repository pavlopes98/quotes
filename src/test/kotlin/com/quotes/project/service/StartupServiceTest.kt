package com.quotes.project.service

import com.quotes.project.repository.QuoteRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@ActiveProfiles("test")
@SpringJUnitConfig
@SpringBootTest
internal class StartupServiceTest{

    @MockBean
    private lateinit var quoteRepository: QuoteRepository

    @MockBean
    private lateinit var quoteService: QuoteService

    private lateinit var startupService: StartupService

    @Test
    fun `will load all data from url if count is not maxItems`() {
        `when`(quoteRepository.count()).thenReturn(0L)

        startupService = StartupService(quoteRepository, quoteService)

        startupService.startupCheck()

        verify(quoteService, times(1)).cleanData()
        verify(quoteService, times(1)).loadDataFromURL(50000L)
    }

    @Test
    fun `will not load data from url if count is maxItems`() {
        `when`(quoteRepository.count()).thenReturn(50000L)

        startupService = StartupService(quoteRepository, quoteService)

        startupService.startupCheck()

        verify(quoteService, never()).cleanData()
        verify(quoteService, never()).loadDataFromURL(50000L)
    }
}