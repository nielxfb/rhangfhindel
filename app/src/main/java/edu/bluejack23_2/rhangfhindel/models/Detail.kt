package edu.bluejack23_2.rhangfhindel.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Detail(
    val Campus: String,
    val RoomName: String,
    val Initial: String?,
    val StatusDetails: @RawValue List<List<StatusDetail>>
) : Parcelable
