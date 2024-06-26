package edu.bluejack23_2.rhangfhindel.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable