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
    UNISSUED(1)
}

enum class TicketType() {
    CAMERA_UNISSUED,
    GALLERY_UNISSUED,
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
    RESERVATION_DETAIL(true),
    RESERVATION_PROGRESS(true),
    MAP(false),
    FEED(false),
    LOGIN(false),
    RESERVATION(false),
    SETTING(true),
    TICKET(false),
    SCHEDULE(false),
    ADDRESS(true),
    OTHER_MY_PAGE(true),
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
            ADDRESS -> R.id.dialog_ticket_address
            OTHER_MY_PAGE -> R.id.fragment_other_my_page
        }
}

enum class FilterType(private val resourceId: Int) {
    ALL(R.string.feed_filter_all),
    MOVIE(R.string.feed_filter_movie),
    PERFORMANCE(R.string.feed_filter_performance),
    CONCERT(R.string.feed_filter_concert),
    PLAY(R.string.feed_filter_play),
    MUSICAL(R.string.feed_filter_musical),
    EXHIBITION(R.string.feed_filter_exhibition),
    ETC(R.string.ticket_etc),
    ;

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

enum class Category(val value: String) {
    MOVIE("영화") {
        override fun getKey(): String {
            return "MOVIE"
        }

    },
    PERFORMANCE("공연"){
        override fun getKey(): String {
            return "PERFORMANCE"
        }
    },
    PLAY("연극") {
        override fun getKey(): String = "PLAY"
    },
    MUSICAL("뮤지컬") {
        override fun getKey(): String = "MUSICAL"
    },
    CONCERT("콘서트") {
        override fun getKey(): String = "CONCERT"
    },
    EXHIBITION("전시회") {
        override fun getKey(): String = "EXHIBITION"
    },
    ETC("기타") {
        override fun getKey(): String = "ETC"
    }
    ;

    abstract fun getKey(): String


    companion object {
        fun getTitle(value: String): Category? = entries.find { it.value == value }

        fun getKeyResult(value : String) : String = entries.find { it.toString() == value }?.value.toString()
    }


}

enum class PaymentResult(val value: Int) {
    COMPLETE(0),
    REFUND(1),
    USE(2);


    companion object {

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

enum class SeatClass(val value: String) {
    R("R"),
    S("S"),
    A("A"),
    B("B");
}

enum class ShowType {
    KEYWORD,
    DATE,
    LOCATION,
    CATEGORY
}

enum class Area(val value: String, val abbreviation: String){
    SEOUL("서울특별시", "서울"),
    BUSAN("부산광역시", "부산"),
    DAEGU("대구광역시", "대구"),
    INCHEON("인천광역시", "인천"),
    GWANGJU("광주광역시", "광주"),
    DAEJEON("대전광역시", "대전"),
    ULSAN("울산광역시", "울산"),
    SEJON("세종특별자치시", "세종"),
    GYEONGGI("경기도", "경기"),
    GANGWON("강원특별자치도", "강원"),
    CHUNGCHEONGBUKDO("충청북도", "충북"),
    CHUNGCHEONGNAMDO("충청남도", "충남"),
    JEOLLABUKDO("전라북도", "전북"),
    JEOLLANAMDO("전라남도", "전남"),
    GYEONGSANGBUKDO("경상북도", "경북"),
    GYEONGSANGNAMDO("경상남도", "경남"),
    JEJU("제주특별자치도", "제주"),
    ETC("기타", "기타");

    override fun toString(): String {
        return this.name
    }
    companion object {
        val AREA_CODE_00 : List<Area> = listOf()
        val AREA_CODE_10 : List<Area> = listOf(SEOUL)
        val AREA_CODE_20 : List<Area> = listOf(GYEONGGI, INCHEON)
        val AREA_CODE_30 : List<Area> = listOf(GANGWON)
        val AREA_CODE_40 : List<Area> = listOf(CHUNGCHEONGBUKDO, CHUNGCHEONGNAMDO, DAEJEON, SEJON)
        val AREA_CODE_50 : List<Area> = listOf(GYEONGSANGBUKDO, GYEONGSANGNAMDO, DAEGU, ULSAN, BUSAN)
        val AREA_CODE_60 : List<Area> = listOf(JEOLLABUKDO, JEOLLANAMDO, GWANGJU, JEJU)
        val AREA_CODE_70 : List<Area> = listOf(ETC)

        val AREA_PAIR_LIST = listOf(
            Pair("전국", AREA_CODE_00),
            Pair("서울", AREA_CODE_10),
            Pair("경기·인천", AREA_CODE_20),
            Pair("강원", AREA_CODE_30),
            Pair("충청·대전·세종", AREA_CODE_40),
            Pair("경상·대구·울산·부산", AREA_CODE_50),
            Pair("전라·광주·제주", AREA_CODE_60),
            Pair("기타", AREA_CODE_70)
        )

    }
}