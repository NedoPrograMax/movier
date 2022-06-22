package com.example.themovier.firebase

import com.example.themovier.model.MovierUser
import com.google.firebase.firestore.FirebaseFirestore

fun createUser(email: String, userId: String) {
    val name = email.split("@")[0]
    val user = MovierUser(
        userId = userId.toString(),
        name = name,
        email = email,
        profileUrl = "")
    FirebaseFirestore.getInstance().collection("users")
        .add(user)
}