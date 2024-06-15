package edu.bluejack23_2.rhangfhindel.modules

class Response<T> (
    var success: Boolean,
    var message: String,
    var data: T
)