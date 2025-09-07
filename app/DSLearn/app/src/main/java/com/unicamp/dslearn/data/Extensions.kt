package com.unicamp.dslearn.data

import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
suspend fun <R, T> Response<T>.fold(
    onSuccess: suspend (value: T?) -> R,
    onFailure: (error: ResponseBody?) -> R
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }

    return if (this.isSuccessful) {
        onSuccess(this.body())
    } else {
        onFailure(this.errorBody())
    }
}