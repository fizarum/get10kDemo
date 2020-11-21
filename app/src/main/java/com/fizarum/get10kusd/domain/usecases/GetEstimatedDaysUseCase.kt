package com.fizarum.get10kusd.domain.usecases

import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User

class GetEstimatedDaysUseCase : SimpleUseCase<Int, GetEstimatedDaysUseCase.Params>() {

    override fun buildUseCaseSimple(params: Params): Int {

        val targetAmount = params.goal.amountOfMoney
        val dailyWage = params.user.dailyWage

        val daysWithHours = targetAmount.toFloat().div(dailyWage)
        val justDays = daysWithHours.toInt()
        val hours = daysWithHours.minus(justDays)
        return if (hours > 0f) justDays + 1 else justDays
    }

    class Params(
        val user: User,
        val goal: Goal
    )
}