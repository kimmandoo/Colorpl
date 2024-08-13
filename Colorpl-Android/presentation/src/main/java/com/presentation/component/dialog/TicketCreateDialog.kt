package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogTicketBinding
import com.presentation.base.BaseDialog
import com.presentation.util.TicketState
import com.presentation.util.TicketType

class TicketCreateDialog(
    context: Context,
    private val type: TicketState,
    private val action: (TicketType) -> Unit,
) :
    BaseDialog<DialogTicketBinding>(context, R.layout.dialog_ticket) {
    override fun onCreateDialog() {
        when (type) {
            TicketState.UNISSUED -> {
                binding.apply {
                    tvCamera.setOnClickListener {
                        action(TicketType.CAMERA_UNISSUED)
                        dismiss()
                    }
                    tvGallery.setOnClickListener {
                        action(TicketType.GALLERY_UNISSUED)
                        dismiss()
                    }
                }
            }
        }
    }
}