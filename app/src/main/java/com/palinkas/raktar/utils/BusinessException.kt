package com.palinkas.raktar.utils

import androidx.annotation.StringRes

class BusinessException(
    msg: String,
    @param:StringRes val errorMsgId: Int) : Exception(msg) {
}