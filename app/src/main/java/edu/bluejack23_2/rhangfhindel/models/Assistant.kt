package edu.bluejack23_2.rhangfhindel.models

data class Assistant(
    val UserId: String,
    val Name: String,
    val Username: String,
    val PictureId: String,
    val Roles: ArrayList<String>,
    var UseBiometric: Boolean = false
)