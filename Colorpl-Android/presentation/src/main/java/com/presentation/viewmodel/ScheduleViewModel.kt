package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.domain.model.CalendarItem
import com.domain.model.Ticket
import com.presentation.util.Calendar
import com.presentation.util.CalendarMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(

) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _clickedDate = MutableStateFlow<CalendarItem?>(null)
    val clickedDate: StateFlow<CalendarItem?> = _clickedDate

    private val _calendarItems = MutableStateFlow<List<CalendarItem>>(emptyList())
    val calendarItems: StateFlow<List<CalendarItem>> = _calendarItems

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    private val _displayDate = MutableStateFlow("")
    val displayDate: StateFlow<String> = _displayDate

    private val _calendarMode = MutableStateFlow(CalendarMode.MONTH)
    val calendarMode: StateFlow<CalendarMode> = _calendarMode

    init {
        updateCalendar(Calendar.CURRENT)
    }

    fun setClickedDate(calendarItem: CalendarItem) {
        _clickedDate.value = calendarItem
        _selectedDate.value = calendarItem.date
    }

    fun handleItemClick(
        clickedItem: CalendarItem,
    ) {
        _calendarMode.value = CalendarMode.WEEK
        val updatedList =
            getOnlySelectedWeek(clickedItem.date, _calendarItems.value).map { item ->
                item.copy(
                    isSelected = item.date == clickedItem.date,
                    isWeek = true
                )
            }
        _calendarItems.value = updatedList
        _selectedDate.value = clickedItem.date
        _clickedDate.value = clickedItem.copy(isSelected = true, isWeek = true)
        updateDisplayDate()
    }

    fun updateCalendar(
        state: Calendar,
        month: Long = 0,
        year: Long = 0,
    ) {
        _selectedDate.value = when (state) {
            Calendar.CURRENT -> {
                LocalDate.now()
            }

            Calendar.NEXT -> {
                _selectedDate.value.plusMonths(1)
            }

            Calendar.PREVIOUS -> {
                _selectedDate.value.minusMonths(1)
            }

            Calendar.CHANGE -> {
                val changedYear = year - _selectedDate.value.year
                val changedMonth = month - _selectedDate.value.monthValue

                _selectedDate.value.plusYears(changedYear).plusMonths(changedMonth)
            }

            Calendar.RESTORE -> {
                _calendarMode.value = CalendarMode.MONTH
                _clickedDate.value?.date
            }
        }

        val updateList = if (state == Calendar.RESTORE) {
            createCalendar(_selectedDate.value).map { item ->
                item.copy(
                    isSelected = item.date == _selectedDate.value,
                    isWeek = false  // 월 모드로 돌아갈 때 isWeek를 false로 설정
                )
            }
        } else {
            createCalendar(_selectedDate.value)
        }
        _calendarItems.value = updateList
        _calendarMode.value = CalendarMode.MONTH
        updateDisplayDate()
    }

    fun updateCalendarWeekMode(
        state: Calendar
    ) {
        _selectedDate.value = when (state) {
            Calendar.CURRENT -> {
                LocalDate.now()
            }

            Calendar.NEXT -> {
                _selectedDate.value.plusWeeks(1)
            }

            Calendar.PREVIOUS -> {
                _selectedDate.value.plusWeeks(-1)
            }

            else -> {
                null
            }
        }

        _calendarItems.value = getOnlySelectedWeek(
            _selectedDate.value,
            createCalendar(_selectedDate.value)
        ).map { item ->
            item.copy(
                isSelected = item.date == _clickedDate.value?.date,
                isWeek = true  // 주간 모드에서는 모든 아이템의 isWeek를 true로 설정
            )
        }
        updateDisplayDate()
    }

    private fun findStartOfWeek(targetDate: LocalDate): LocalDate {
        var current = targetDate
        while (current.dayOfWeek != DayOfWeek.SUNDAY) {
            current = current.minusDays(1)
        }
        return current
    }

    private fun getOnlySelectedWeek(
        targetDate: LocalDate,
        currentList: List<CalendarItem>
    ): List<CalendarItem> {
        val startOfWeek = findStartOfWeek(targetDate = targetDate)
        val endOfWeek = startOfWeek.plusDays(6)

        val updatedList = currentList.filter { item ->
            item.date >= startOfWeek && item.date <= endOfWeek
        }
        return updatedList
    }

    private fun updateDisplayDate() {
        val date = _selectedDate.value
        val (currentYear, currentMonth) = date.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
            .split(" ")
        _displayDate.value = if (_calendarMode.value == CalendarMode.WEEK) {
            val weekFields = WeekFields.of(Locale.getDefault())
            val weekOfMonth = date.get(weekFields.weekOfMonth())
            "$currentYear $currentMonth ${weekOfMonth}주"
        } else {
            "$currentYear $currentMonth"
        }
    }

    private fun createCalendar(targetDate: LocalDate): List<CalendarItem> {
        val days = mutableListOf<CalendarItem>()
        val yearMonth = YearMonth.from(targetDate)
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

}