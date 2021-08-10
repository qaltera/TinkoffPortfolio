package com.qaltera.tinkoffportfolio.data

class ResponseDto<T> (
    val trackingId: String,
    val status: String,
    val payload: T
)