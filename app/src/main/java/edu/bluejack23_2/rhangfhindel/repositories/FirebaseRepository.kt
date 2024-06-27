package edu.bluejack23_2.rhangfhindel.repositories

import android.content.Context
import com.google.firebase.database.*
import edu.bluejack23_2.rhangfhindel.models.Booker
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.PopUp

object FirebaseRepository {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    suspend fun addBooker(roomNumber: String, initial: String) {
        Coroutines.main {
            val key = db.child("bookers").push().key.toString()
            db.child("bookers").child(key).setValue(Booker(initial, roomNumber))
            val generation = initial.substring(2)
            NotificationRepository.sendNotification(
                "Rang for $generation",
                "Rang has been booked at $roomNumber",
                generation
            )
        }
    }

    fun getBookers(context: Context, callback: (List<String>, List<String>) -> Unit) {
        db.child("bookers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomList = mutableListOf<String>()
                val initialList = mutableListOf<String>()
                for (data in snapshot.children) {
                    val booker = data.getValue(Booker::class.java)
                    booker?.let { roomList.add(it.roomNumber!!) }
                    booker?.let { initialList.add(it.initial!!) }
                }
                callback(roomList, initialList)
            }

            override fun onCancelled(error: DatabaseError) {
                PopUp.longDuration(context, error.message)
            }

        })
    }

}