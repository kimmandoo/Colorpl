package com.presentation.component.dialog

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.colorpl.presentation.R

class LocationPickerDialog(
    context: Context,
    private val locationList: Array<String>,
    private val onLocationSelected: (String) -> Unit // 콜백 함수로 선택된 위치를 전달
) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.CustomAlertDialogListItem)

    init {
        setupDialog()
    }

    private fun setupDialog() {
        builder.setTitle("지역 선택")
        builder.setItems(locationList) { dialog, which ->
            val selectedCity = locationList[which]
            onLocationSelected(selectedCity)
            dialog.dismiss()
        }
    }

    fun show() {
        builder.show()
    }
}