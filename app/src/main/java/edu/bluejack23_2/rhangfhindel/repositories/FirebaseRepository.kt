package edu.bluejack23_2.rhangfhindel.repositories

import android.content.Context
import com.google.firebase.database.*
import edu.bluejack23_2.rhangfhindel.models.Booker
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.PopUp

object FirebaseRepository {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun addBooker(roomNumber: String, initial: String) {
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

    fun getBookers(context: Context, callback: (List<Booker>) -> Unit) {
        db.child("bookers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookerList = mutableListOf<Booker>()
                for (data in snapshot.children) {
                    val booker = data.getValue(Booker::class.java)
                    booker?.let { bookerList.add(it) }
                }
                callback(bookerList)
            }

            override fun onCancelled(error: DatabaseError) {
                PopUp.longDuration(context, error.message)
            }

        })
    }

}