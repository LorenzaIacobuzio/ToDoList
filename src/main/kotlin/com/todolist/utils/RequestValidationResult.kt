package com.todolist.utils

sealed class RequestValidationResult {
    data object Valid : RequestValidationResult()
    data class Invalid(val errorMessage: String) : RequestValidationResult()
}
