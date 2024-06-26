package edu.bluejack23_2.rhangfhindel.modules.requests

data class NotificationRequest(
    val title: String,
    val body: String,
    val onlyForGeneration: String
)