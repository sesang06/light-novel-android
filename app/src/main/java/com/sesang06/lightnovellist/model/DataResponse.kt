package com.sesang06.lightnovellist.model

class DataResponse<T> {
    val code: Int
    val message: String
    val data: T

    constructor(code: Int, message: String, data: T) {
        this.code = code
        this.message = message
        this.data = data
    }

}