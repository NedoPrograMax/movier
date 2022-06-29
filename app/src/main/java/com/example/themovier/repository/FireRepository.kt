package com.example.themovier.repository

import android.util.Log
import com.example.themovier.data.DataOrException
import com.example.themovier.data.Resource
import com.example.themovier.model.MovierItem
import com.example.themovier.model.MovierUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireRepository @Inject constructor(private val queryUser: Query) {
     suspend fun getUserInfo(userId: String): DataOrException<MovierUser, Exception> {
       val dataOrException = DataOrException<MovierUser, Exception>()

        try {
            dataOrException.data = queryUser.whereEqualTo("userId", userId).get().await().map {document->
               document.toObject(MovierUser::class.java)
            }.first()

        }catch (e: FirebaseFirestoreException){
            dataOrException.e = e
        }

       return dataOrException
    }

    suspend fun getUserMovies(userId: String): DataOrException<List<MovierItem>, Exception>{
        val dataOrException = DataOrException<List<MovierItem>, Exception>()
        try{
            dataOrException.data = FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("userId", userId).get().await().map {document->
                    document.toObject(MovierItem::class.java)
                }
        }catch (e:FirebaseFirestoreException){
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getMovie(movieId: String): DataOrException<MovierItem, Exception>{
        val dataOrException = DataOrException<MovierItem, Exception>()
        try{
            dataOrException.data = FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("id", movieId).get().await().map {document->
                    document.toObject(MovierItem::class.java)
                }.first()
        }catch (e:FirebaseFirestoreException){
            dataOrException.e = e
        }
        return dataOrException
    }
}