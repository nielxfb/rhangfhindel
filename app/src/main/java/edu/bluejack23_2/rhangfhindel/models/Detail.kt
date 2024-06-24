package edu.bluejack23_2.rhangfhindel.models

data class Detail(
    val Campus: String,
    val RoomName: String,
    val StatusDetails: List<List<StatusDetail>>
)
