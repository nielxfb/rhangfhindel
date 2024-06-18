package edu.bluejack23_2.rhangfhindel.modules.responses

data class RoomTransactionResponse(
    val Details: List<Detail>
)

data class Detail(
    val Campus: String,
    val RoomName: String,
    val StatusDetails: List<List<StatusDetail>>
)

data class StatusDetail(
    val Assistant: String?,
    val ClassName: String?,
    val Description: String?,
    val Division: String?,
    val Email: String?,
    val InsertedDate: String?,
    val Name: String?,
    val NeedInternet: Boolean,
    val Softwares: List<String>?,
    val Status: String,
    val StudentOnsiteStatus: String,
    val Subject: String?,
    val TransactionDetailId: String?,
    val TransactionId: String?,
    val Type: String?
)