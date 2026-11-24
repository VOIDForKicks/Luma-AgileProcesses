package com.example.luma_project.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val rtdbRef = FirebaseDatabase.getInstance().getReference("users")
    private val firestoreDb = FirebaseFirestore.getInstance()

    suspend fun pushUserToRealtimeDatabase(user: User) {
        if (user.UserID.isNotEmpty()) {
            rtdbRef.child(user.UserID).setValue(user).await()
        }
    }

    suspend fun pushUserToFirestore(user: User) {
        if (user.UserID.isNotEmpty()) {
            firestoreDb.collection("users").document(user.UserID).set(user).await()
        }
    }
}
