package com.presentation.util

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import com.domain.model.CalendarItem
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.Flag
import com.presentation.component.adapter.schedule.CalendarAdapter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * 선택한 날의 주차 시작점 찾기
 */
private fun LocalDate.findStartOfWeek(): LocalDate {
    var current = this
    while (current.dayOfWeek != DayOfWeek.SUNDAY) {
        current = current.minusDays(1)
    }
    return current
}

/**
 * 시작점 찾아와서 그 주를 가져오기
 * @param calendarAdapter : 업데이트 할 어댑터
 */
fun LocalDate.getOnlySelectedWeek(calendarAdapter: CalendarAdapter): List<CalendarItem> {
    val startOfWeek = findStartOfWeek()
    val endOfWeek = startOfWeek.plusDays(6)

    val updatedList = calendarAdapter.currentList.filter { item ->
        item.date >= startOfWeek && item.date <= endOfWeek
    }
    return updatedList
}

/**
 * 달력을 생성하는 함수
 */
fun LocalDate.createCalendar(): List<CalendarItem> {
    val days = mutableListOf<CalendarItem>()
    val yearMonth = YearMonth.from(this)
    val firstOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val today = LocalDate.now()

    // 이전 달의 날짜 추가
    val dayOfWeek = firstOfMonth.dayOfWeek.value
    val previousMonth = yearMonth.minusMonths(1)
    val previousMonthDays = previousMonth.lengthOfMonth()
    for (i in 0 until dayOfWeek) {
        val currentDate = previousMonth.atDay(previousMonthDays - dayOfWeek + i + 1)
        days.add(
            CalendarItem(
                date = currentDate,
                isSunday = currentDate.dayOfWeek == DayOfWeek.SUNDAY,
                isCurrentMonth = false,
                isToday = currentDate.isEqual(today)
            )
        )
    }
    // 현재 달의 날짜 추가
    for (i in 1..daysInMonth) {
        val currentDate = firstOfMonth.plusDays(i - 1L)

        days.add(
            CalendarItem(
                date = currentDate,
                isSunday = currentDate.dayOfWeek == DayOfWeek.SUNDAY,
                isCurrentMonth = true,
                isToday = currentDate.isEqual(today)
            )
        )
    }

    // 다음 달의 날짜 추가
    val nextMonth = yearMonth.plusMonths(1)
    for (i in 1 until 43 - (dayOfWeek + daysInMonth)) {
        val currentDate = nextMonth.atDay(i)
        days.add(
            CalendarItem(
                date = currentDate,
                isSunday = currentDate.dayOfWeek == DayOfWeek.SUNDAY,
                isCurrentMonth = false,
                isToday = currentDate.isEqual(today)
            )
        )
    }

    return days
}

fun RecyclerView.overScrollControl(logic: (Int, Float)-> Unit){
    edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(
            recyclerView: RecyclerView,
            direction: Int
        ): EdgeEffect {
            return object : EdgeEffect(recyclerView.context) {
                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    logic(direction, deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    logic(direction, deltaDistance)
                }
            }
        }
    }
}

