package edu.bluejack23_2.rhangfhindel.models

data class APIToken (
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String
)