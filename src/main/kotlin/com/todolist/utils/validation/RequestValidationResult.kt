package com.todolist.utils.validation

sealed class RequestValidationResult {
    data object Valid : RequestValidationResult()
    data class Invalid(val errorMessage: String) : RequestValidationResult()
}
