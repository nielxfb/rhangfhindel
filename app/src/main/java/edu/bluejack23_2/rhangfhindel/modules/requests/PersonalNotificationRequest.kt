package edu.bluejack23_2.rhangfhindel.modules.requests

data class PersonalNotificationRequest(
    val title: String,
    val body: String,
    val token: String
)