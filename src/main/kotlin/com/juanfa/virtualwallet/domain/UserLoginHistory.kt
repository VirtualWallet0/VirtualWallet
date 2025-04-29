package com.juanfa.virtualwallet.domain

data class UserLoginHistory(
    val id: String,
    val date: Long,
    val status: LoginStatus
){

}

enum class LoginStatus {
    SUCCESS, FAILURE
}
