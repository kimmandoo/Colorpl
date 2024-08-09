package com.presentation.util

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import timber.log.Timber

/**
 * 결제할 아이템 하나에 대한 return
 * 이걸 각 아이템별로 호출해서 List로 만들어 결제요청
 * @param itemCode
 * @param itemName
 * @param itemPrice
 * @param itemQuantity
 */
fun selectItemToPay(
    itemName: String,
    itemCode: String,
    itemQuantity: Int,
    itemPrice: Double
): BootItem {
    return BootItem().setName(itemName).setId(itemCode).setQty(itemQuantity).setPrice(itemPrice)
}

/**
 *  일시불만 가능하게 함
 *  @param itemList : selectItemToPay에서 가져온 아이템들을 리스트로 묶어서 넣어줘야됨
 */


/**
 * 서버에 요청해서 재곻 확인할 함수
 */
private fun checkClientValidation(data: String): Boolean {

    return false
}


fun getBootUser(
    userId: String,
    email: String,
    phone: String,
    username: String,
): BootUser {
    val user = BootUser()
    user.id = userId
    user.email = email
    user.phone = phone
    user.username = username
    return user
}


fun requestPayment(
    v: View?,
    applicationId: String,
    user: BootUser,
    itemList: MutableList<BootItem>,
    orderName: String,
    orderId: String,
    context: Context,
    manager: FragmentManager,
    checkClientValidation: (String) -> Unit
) {
    val payload = Payload()
    var totalPrice: Double = 0.0
    itemList.forEachIndexed { index, bootItem ->
        totalPrice += bootItem.price
    }
    payload.setApplicationId(applicationId)
        .setOrderName(orderName)
        .setOrderId(orderId)
        .setPrice(totalPrice)
        .setUser(user)

    val map: MutableMap<String, Any> = HashMap()
    map["1"] = "abcdef"
    map["2"] = "abcdef55"
    map["3"] = 1234
    payload.metadata = map
    Bootpay.init(manager, context)
        .setPayload(payload)
        .setEventListener(object : BootpayEventListener {
            override fun onCancel(data: String) {
                Timber.tag("bootpay").d("cancel: $data")
            }

            override fun onError(data: String) {
                Timber.tag("bootpay").d("error: $data")
            }

            override fun onClose() {
                Bootpay.removePaymentWindow()
            }

            override fun onIssued(data: String) {
                Timber.tag("bootpay").d("issued: $data")
            }

            override fun onConfirm(data: String): Boolean {
                Timber.tag("bootpay").d("receipt $data")
                checkClientValidation(data)
                Bootpay.dismissWindow()
                return false
            }

            override fun onDone(data: String) {
                Timber.tag("bootpay").d("done: $data")
            }
        }).requestPayment()
}


