package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.CalendarItem
import com.domain.model.TicketResponse
import com.domain.usecase.TicketUseCase
import com.domain.util.DomainResult
import com.presentation.util.Calendar
import com.presentation.util.CalendarMode
import com.presentation.util.getPattern
import com.presentation.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val ticketUseCase: TicketUseCase
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _clickedDate = MutableStateFlow<CalendarItem?>(null)
    val clickedDate: StateFlow<CalendarItem?> = _clickedDate

    private val _calendarItems = MutableStateFlow<List<CalendarItem>>(emptyList())
    val calendarItems: StateFlow<List<CalendarItem>> = _calendarItems

    private val _tickets = MutableSharedFlow<List<TicketResponse>>(replay = 1)
    val tickets: SharedFlow<List<TicketResponse>> = _tickets.asSharedFlow()

    private val _filteredTickets = MutableSharedFlow<List<TicketResponse>>(replay = 1)
    val filteredTickets: SharedFlow<List<TicketResponse>> = _filteredTickets.asSharedFlow()

    private val _displayDate = MutableStateFlow("")
    val displayDate: StateFlow<String> = _displayDate

    private val _calendarMode = MutableStateFlow(CalendarMode.MONTH)
    val calendarMode: StateFlow<CalendarMode> = _calendarMode

    init {
        updateCalendar(Calendar.CURRENT)
    }

    fun refreshTickets() {
        viewModelScope.launch {
            val currentPattern = _selectedDate.value.getPattern("yyyy-MM-dd")
            ticketUseCase.getMonthlyTicket(currentPattern).collect { result ->
                when (result) {
                    is DomainResult.Error -> {
                        Timber.tag("tickets").d("refresh: ${result.exception}")
                    }

                    is DomainResult.Success -> {
                        _tickets.emit(result.data)

                        // 현재 캘린더 모드에 따라 적절히 처리
                        when (_calendarMode.value) {
                            CalendarMode.MONTH -> {
                                _calendarItems.value =
                                    matchTicketsToCalendar(_calendarItems.value, result.data)
                                _tickets.emit(result.data)
                            }

                            CalendarMode.WEEK -> {
                                forceUpdate()
                                val updatedWeekItems =
                                    getOnlySelectedWeek(_selectedDate.value, _calendarItems.value)
                                _calendarItems.value =
                                    matchTicketsToCalendar(updatedWeekItems, result.data)
                                _tickets.emit(result.data)
                            }
                        }

                        Timber.tag("tickets").d("새로고침됨")
                    }
                }
            }
        }
    }

    fun matchTicketsToCalendar(
        calendarItems: List<CalendarItem>,
        tickets: List<TicketResponse>
    ): List<CalendarItem> {
        val ticketMap = tickets.associateBy { it.dateTime.toLocalDate() }

        return calendarItems.map { calendarItem ->
            val matchingTicket = ticketMap[calendarItem.date]
            if (matchingTicket != null) {
                calendarItem.copy(imgUrl = matchingTicket.imgUrl ?: "")
            } else {
                calendarItem
            }
        }
    }

    fun filterTicketsForSelectedWeek() {
        viewModelScope.launch {
            val startOfWeek = findStartOfWeek(_selectedDate.value)
            val endOfWeek = startOfWeek.plusDays(6)

            val filteredList = _tickets.replayCache.firstOrNull()?.filter { ticket ->
                val ticketDate = ticket.dateTime.toLocalDate()
                ticketDate in startOfWeek..endOfWeek
            } ?: emptyList()
            _filteredTickets.emit(filteredList)
        }
    }


    fun getMonthlyTicket(pattern: String) {
        viewModelScope.launch {
            ticketUseCase.getMonthlyTicket(pattern).collect {
                when (it) {
                    is DomainResult.Error -> {
                        Timber.tag("tickets").d("${it.exception}")
                    }

                    is DomainResult.Success -> {
                        _tickets.emit(it.data)
                        _calendarItems.value = matchTicketsToCalendar(_calendarItems.value, it.data)
                    }
                }
            }
        }
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
        Timber.tag("calendar").d("$clickedItem \n $updatedList")

        _calendarItems.value = updatedList
        _selectedDate.value = clickedItem.date
        _clickedDate.value = clickedItem.copy(isSelected = true, isWeek = true)
        updateDisplayDate()
        filterTicketsForSelectedWeek()
    }

    fun swipeUpdateCalendar(direction: Int) {
        if (direction == 1) {
            when (_calendarMode.value) {
                CalendarMode.MONTH -> updateCalendar(Calendar.NEXT)
                CalendarMode.WEEK -> updateCalendarWeekMode(Calendar.NEXT)
            }
        } else {
            when (_calendarMode.value) {
                CalendarMode.MONTH -> updateCalendar(Calendar.PREVIOUS)
                CalendarMode.WEEK -> updateCalendarWeekMode(Calendar.PREVIOUS)
            }
        }
    }

    fun restoreCalendarMode() {
        if (_calendarMode.value == CalendarMode.WEEK) {
            _calendarMode.value = CalendarMode.MONTH
            updateCalendar(Calendar.RESTORE)
        }
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
                _clickedDate.value?.date ?: LocalDate.now()
            }
        }
        getMonthlyTicket(_selectedDate.value.getPattern("yyyy-MM-dd"))

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
        viewModelScope.launch {
            _calendarItems.value = matchTicketsToCalendar(updateList, _tickets.last())
            _filteredTickets.emit(_tickets.replayCache.firstOrNull() ?: emptyList())
        }
    }

    fun forceUpdate(){
        val fullMonthCalendar = createCalendar(_selectedDate.value)
        val weekCalendar = getOnlySelectedWeek(_selectedDate.value, fullMonthCalendar)

        _calendarItems.value = weekCalendar.map { item ->
            item.copy(
                isSelected = item.date == _selectedDate.value,
                isWeek = true
            )
        }
        _clickedDate.value = _calendarItems.value.find { it.isSelected }
        updateDisplayDate()
        filterTicketsForSelectedWeek()
    }

    fun updateCalendarWeekMode(
        state: Calendar
    ) {
        val newSelectedDate = when (state) {
            Calendar.CURRENT -> LocalDate.now()
            Calendar.NEXT -> _selectedDate.value.plusWeeks(1)
            Calendar.PREVIOUS -> _selectedDate.value.minusWeeks(1)
            else -> _selectedDate.value
        } ?: _selectedDate.value

        _selectedDate.value = newSelectedDate
        val fullMonthCalendar = createCalendar(newSelectedDate)
        val weekCalendar = getOnlySelectedWeek(newSelectedDate, fullMonthCalendar)

        _calendarItems.value = weekCalendar.map { item ->
            item.copy(
                isSelected = item.date == newSelectedDate,
                isWeek = true
            )
        }
        _clickedDate.value = _calendarItems.value.find { it.isSelected }
        updateDisplayDate()
        filterTicketsForSelectedWeek()
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