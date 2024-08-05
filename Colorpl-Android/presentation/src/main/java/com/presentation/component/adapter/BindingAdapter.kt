package com.presentation.component.adapter

import android.graphics.drawable.Drawable
import android.provider.Settings.Global.getString
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.domain.model.Seat
import com.presentation.util.Category
import com.presentation.util.PaymentResult
import com.presentation.util.Sign
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@BindingAdapter("setImage")
fun loadImage(imageView: ImageView, image: Drawable?) {
    Glide.with(imageView.context)
        .load(image)
        .into(imageView)
}

@BindingAdapter("setSignHint")
fun setSignHint(editText: EditText, type: Sign?) {
    val context = editText.context
    type?.run {
        val text = when (type) {
            Sign.ID -> {
                context.getString(R.string.login_id_hint)
            }

            Sign.PASSWORD -> {
                context.getString(R.string.login_password_hint)
            }

            else -> {
                context.getString(R.string.login_nickname_hint)
            }
        }
        editText.setHint(text)
    }
}

@BindingAdapter("setSignUpHint")
fun setSignUpHint(textView: TextView, type: Sign?) {
    val context = textView.context
    type?.run {
        val text = when (type) {
            Sign.ID -> {
                context.getString(R.string.sign_up_id_hint)
            }

            Sign.PASSWORD -> {
                context.getString(R.string.sign_up_password_hint)
            }

            Sign.NICKNAME -> {
                context.getString(R.string.sign_up_nickname_hint)
            }

            else -> {
                context.getString(R.string.sign_up_profile_image_hint)
            }

        }
        textView.setText(text)
    }
}

@BindingAdapter("setSignTitle")
fun setSignTitle(textView: TextView, type: Sign?) {
    val context = textView.context
    type?.run {
        val text = when (type) {
            Sign.ID -> {
                context.getString(R.string.sign_up_id)
            }

            Sign.PASSWORD -> {
                context.getString(R.string.sign_up_password)
            }

            else -> {
                context.getString(R.string.sign_up_nickname)
            }
        }
        textView.text = text
    }
}

@BindingAdapter("setSignMark")
fun setSignMark(imageView: ImageView, type: Sign?) {
    val context = imageView.context
    type?.run {
        val image = when (type) {
            Sign.ID -> {
                ContextCompat.getDrawable(context, R.drawable.ic_id_mark)
            }

            Sign.PASSWORD -> {
                ContextCompat.getDrawable(context, R.drawable.ic_password_mark)
            }

            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_nickname_mark)
            }
        }
        loadImage(imageView, image)
    }
}


//Category
@BindingAdapter("setCategoryTitle")
fun setCategoryTitle(textView: TextView, category: Category?) {
    val context = textView.context
    category?.run {
        val text = when (category) {
            Category.MOVIE -> {
                context.getString(R.string.sign_up_category_movie)
            }

            Category.THEATRE -> {
                context.getString(R.string.sign_up_category_theater)
            }

            Category.MUSICAL -> {
                context.getString(R.string.sign_up_category_musical)
            }

            Category.CIRCUS -> {
                context.getString(R.string.sign_up_category_circus)
            }

            Category.EXHIBITION -> {
                context.getString(R.string.sign_up_category_exhibition)
            }
        }
        textView.text = text
    }
}

@BindingAdapter("setCategoryIcon")
fun setCategoryIcon(imageView: ImageView, category: Category?) {
    val context = imageView.context
    category?.run {
        val image = when (category) {
            Category.MOVIE -> {
                ContextCompat.getDrawable(context, R.drawable.selector_ic_movie)
            }

            Category.THEATRE -> {
                ContextCompat.getDrawable(context, R.drawable.selector_ic_theatre)
            }

            Category.MUSICAL -> {
                ContextCompat.getDrawable(context, R.drawable.selector_ic_musical)
            }

            Category.CIRCUS -> {
                ContextCompat.getDrawable(context, R.drawable.selector_ic_circus)
            }

            Category.EXHIBITION -> {
                ContextCompat.getDrawable(context, R.drawable.selector_ic_exhibition)
            }
        }
        loadImage(imageView, image)
    }
}


@BindingAdapter("setPaymentBackground")
fun setPaymentBackground(imageView: ImageView, paymentResult: PaymentResult?) {
    val context = imageView.context
    paymentResult?.run {
        val image = when (paymentResult) {
            PaymentResult.COMPLETE -> {

                ContextCompat.getDrawable(context, R.color.transparent)
            }

            PaymentResult.REFUND -> {
                ContextCompat.getDrawable(context, R.drawable.rectangle_eerie_black_fifty_10)
            }

            PaymentResult.USE -> {
                ContextCompat.getDrawable(context, R.drawable.rectangle_white_twenty_10)
            }
        }
        loadImage(imageView, image)
    }
}

@BindingAdapter("setFormattedSeatsText")
fun setFormattedSeatsText(textView: TextView, seats: List<Seat>?) {
    seats?.let {
        val seatText = "${seats.size}매 | ${seats.joinToString(separator = ", ") { it.toString() }}"
        textView.text = seatText
    }
}

@BindingAdapter("dayOfWeekColor", "isSelected")
fun setDayOfWeekColor(view: TextView, date: LocalDate, isSelected: Boolean) {
    val context = view.context
    val dayOfWeek = date.dayOfWeek
    val colorRes = when {
        isSelected -> R.color.white
        date.isBefore(LocalDate.now()) -> R.color.light_gray // 선택 불가능한 색
        dayOfWeek == java.time.DayOfWeek.SATURDAY -> R.color.blue
        dayOfWeek == java.time.DayOfWeek.SUNDAY -> R.color.imperial_red
        else -> R.color.eerie_black
    }
    view.setTextColor(ContextCompat.getColor(context, colorRes))
}

@BindingAdapter("date", "setTitle")
fun setSelectedDate(view: TextView, date: LocalDate, setTitle: Boolean) {
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val selectedDateTitle = "${view.context.getString(R.string.reservation_selected_title)} : "
    val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    val dayOfWeek = " (${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)})"
    view.text = if (setTitle) {
        val subText = when (date) {
            today -> " ${view.context.getString(R.string.reservation_date_today)}"
            tomorrow -> " ${view.context.getString(R.string.reservation_date_tomorrow)}"
            else -> ""
        }
        selectedDateTitle + formattedDate + dayOfWeek + subText
    } else {
        formattedDate + dayOfWeek
    }

}