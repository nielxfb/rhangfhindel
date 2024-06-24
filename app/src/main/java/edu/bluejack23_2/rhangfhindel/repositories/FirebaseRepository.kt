package edu.bluejack23_2.rhangfhindel.repositories

import android.content.Context
import com.google.firebase.database.*
import edu.bluejack23_2.rhangfhindel.models.Booker
import edu.bluejack23_2.rhangfhindel.utils.PopUp

object FirebaseRepository {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun addBooker(roomNumber: String, initial: String) {
        db.child("bookers").child(roomNumber).setValue(Booker(initial))
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