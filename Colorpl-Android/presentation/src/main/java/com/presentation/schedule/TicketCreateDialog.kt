package com.presentation.schedule

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogTicketBinding
import com.presentation.base.BaseDialog
import com.presentation.util.TicketState
import com.presentation.util.TicketType

class TicketCreateDialog(
    context: Context,
    private val type: TicketState,
    private val action: (TicketType) -> Unit
) :
    BaseDialog<DialogTicketBinding>(context, R.layout.dialog_ticket) {
    override fun onCreateDialog() {
        when (type) {
            TicketState.ISSUED -> {
                binding.apply {
                    tvCamera.setOnClickListener {
                        // 카메라로 보내기 fragment에서 처리하는 게 좋을 것 같다.
                        action(TicketType.CAMERA)
                        dismiss()
                    }
                    tvGallery.setOnClickListener {
                        // 갤러리를 띄워서 사진을 선택하게 하고, 선택했으면 uri 가지고 다음 페이지로 이동하도록
                        action(TicketType.GALLERY)
                        dismiss()
                    }
                }
            }

            TicketState.UNISSUED -> {
                binding.apply {
                    tvCamera.setOnClickListener {
                        // 카메라로 보내기 fragment에서 처리하는 게 좋을 것 같다.
                        action(TicketType.CAMERA)
                        dismiss()
                    }
                    tvGallery.setOnClickListener {
                        // 갤러리를 띄워서 사진을 선택하게 하고, 선택했으면 uri 가지고 다음 페이지로 이동하도록
                        action(TicketType.GALLERY)
                        dismiss()
                    }
                }
            }
        }

    }

}