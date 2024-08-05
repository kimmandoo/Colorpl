package com.presentation.util

import android.content.Context
import android.view.View
import com.colorpl.presentation.R
import com.presentation.component.custom.DropDownData

enum class Mode(val mode: String) {
    WALK("WALK"),
    BUS("BUS"),
    SUBWAY("SUBWAY"),
    EXPRESS_BUS("EXPRESS BUS"),
    TRAIN("TRAIN"),
    AIRPLANE("AIRPLANE"),
    FERRY("FERRY"),
}

enum class ReviewMode {
    NEW,
    EDIT
}

enum class TicketState(val state: Int) {
    ISSUED(0),
    UNISSUED(1)
}

enum class TicketType() {
    CAMERA,
    GALLERY
}

enum class Calendar {
    CURRENT,
    NEXT,
    PREVIOUS,
    CHANGE,
    RESTORE
}

enum class CalendarMode {
    MONTH, WEEK
}

enum class Page(val hideBottomNav: Boolean) {
    NOTIFICATION(true),
    FEED_DETAIL(true),
    PROFILE_UPDATE(true),
    USER_SEARCH(true),
    MY_REVIEW(true),
    PAYMENT_HISTORY(true),
    NOTICE(true),
    TICKET_CREATE(true),
    TICKET_FINISH(true),
    RESERVATION_DETAIL(true),
    RESERVATION_PROGRESS(true),
    MAP(false),
    FEED(false),
    LOGIN(false),
    RESERVATION(false),
    SETTING(false),
    TICKET(false),
    SCHEDULE(false),
    ADDRESS(true),

    ;

    companion object {
        fun fromId(id: Int): Page? = entries.find { it.id == id }
    }

    val id: Int
        get() = when (this) {
            NOTIFICATION -> R.id.fragment_notification
            FEED_DETAIL -> R.id.fragment_feed_detail
            PROFILE_UPDATE -> R.id.fragment_profile_update
            USER_SEARCH -> R.id.fragment_user_search
            MY_REVIEW -> R.id.fragment_my_review
            PAYMENT_HISTORY -> R.id.fragment_payment_history
            NOTICE -> R.id.fragment_notice
            TICKET_CREATE -> R.id.fragment_ticket_create
            RESERVATION_DETAIL -> R.id.fragment_reservation_detail
            RESERVATION_PROGRESS -> R.id.fragment_reservation_progress
            MAP -> R.id.fragment_map
            FEED -> R.id.fragment_feed
            LOGIN -> R.id.fragment_login
            RESERVATION -> R.id.fragment_reservation
            SETTING -> R.id.fragment_setting
            TICKET -> R.id.fragment_ticket
            SCHEDULE -> R.id.fragment_schedule
            TICKET_FINISH -> R.id.fragment_ticket_finish
            ADDRESS -> R.id.dialog_ticket_address
        }
}

enum class FilterType(private val resourceId: Int) {
    ALL(R.string.feed_filter_all),
    MOVIE(R.string.feed_filter_movie),
    PERFORMANCE(R.string.feed_filter_performance),
    CONCERT(R.string.feed_filter_concert),
    PLAY(R.string.feed_filter_play),
    MUSICAL(R.string.feed_filter_musical),
    EXHIBITION(R.string.feed_filter_exhibition);

    fun getText(context: Context): String {
        return context.getString(resourceId)
    }
}

enum class Sign {
    ID,
    PASSWORD,
    NICKNAME,
    PROFILE,
    LOGIN
}

enum class Category {
    MOVIE,
    THEATRE,
    MUSICAL,
    CIRCUS,
    EXHIBITION
}

enum class PaymentResult(val value: Int) {
    COMPLETE(0),
    REFUND(1),
    USE(2);


    companion object {

        fun getType(value: Int): PaymentResult =
            entries.find { it.value == value } ?: COMPLETE

        fun getMenu(mode: PaymentResult): List<Int> {
            return when (mode) {
                COMPLETE -> listOf(0, 1)
                REFUND -> listOf(1)
                USE -> listOf(2)
            }
        }
    }
}

enum class DropDownMenu(private val value: Int, private val resourceId: Int) {
    REFUND(0, R.string.drop_down_menu_refund),
    DELETE(1, R.string.drop_down_menu_delete);

    fun getText(context: Context): String {
        return context.getString(resourceId)
    }

    companion object {
        fun getMenu(value: Int): DropDownMenu = entries.find { it.value == value } ?: REFUND

        fun getDropDown(value: List<Int>, context: Context): List<DropDownData> {
            val result = mutableListOf<DropDownData>()
            value.forEach { id ->
                val menu = getMenu(id)
                result.add(DropDownData(menu.getText(context), menu))
            }
            return result
        }

    }

}

/**
 * 상단 버튼들 표시 여부 상태 타입.
 *
 * @property back 뒤로가기 버튼 활성화 유무.
 * @property exit 나가기 버튼 활성화 유무.
 */
enum class TopButtonsStatus(private val back: Int, private val exit: Int) {
    BOTH(View.VISIBLE, View.VISIBLE),
    BACK(View.VISIBLE, View.INVISIBLE),
    EXIT(View.INVISIBLE, View.VISIBLE),
    NONE(View.INVISIBLE, View.INVISIBLE);

    fun getBackVisibility() = back
    fun getExitVisibility() = exit
}

class Payment {
    enum class Discount {
        NONE,
        SSAFY_TRAINEE,
        COUPON;
    };
    enum class Method {
        NONE,
        BOOT,
        SSAFY;
    }
}