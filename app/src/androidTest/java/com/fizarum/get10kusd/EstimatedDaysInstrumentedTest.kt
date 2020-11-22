package com.fizarum.get10kusd

import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.usecases.GetEstimatedDaysUseCase
import org.junit.Assert
import org.junit.Test

class EstimatedDaysInstrumentedTest {

    private val userJohnWick = User("id0", "John", "Wick", "", 40)
    private val userCameronDiaz = User("id1", "Cameron", "Diaz", "", 15)

    private val goal10K = Goal(10000)

    @Test
    fun is10KEstimationForJohnWick_isCorrect() {
        val getEstimationUseCase = GetEstimatedDaysUseCase()

        val params = GetEstimatedDaysUseCase.Params(userJohnWick, goal10K)
        val estimatedDays = getEstimationUseCase.execute(params)

        Assert.assertEquals(250, estimatedDays)
    }

    @Test
    fun is10KEstimationForCameronDiaz_isCorrect() {
        val getEstimationUseCase = GetEstimatedDaysUseCase()

        val params = GetEstimatedDaysUseCase.Params(userCameronDiaz, goal10K)
        val estimatedDays = getEstimationUseCase.execute(params)

        Assert.assertEquals(667, estimatedDays)
    }
}