package com.example.themovier.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.firebase.createUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        home: () -> Unit
    ) = viewModelScope.launch{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        createUser(email = task.result.user?.email!!, userId = auth.currentUser?.uid!!)
                        home()
                    }
                    else{
                        task.exception?.message?.let { onFailure(it) }
                    }
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> onFailure(it1) }
                }
        }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailure: (String) -> Unit = {},
        home: () -> Unit
    )
            = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        home()
                    }
                    else{
                        task.exception?.message?.let { onFailure(it) }
                    }
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> onFailure(it1) }
                }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}