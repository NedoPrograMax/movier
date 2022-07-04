package com.example.themovier.data.repo

import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.models.MovierUser
import com.example.themovier.domain.repositories.FireRepo
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepoImpl @Inject constructor(private val queryUser: Query) : FireRepo {
    override suspend fun getUserInfo(userId: String): Result<MovierUser, Throwable> {
        return runCatching {
            queryUser.whereEqualTo("userId", userId).get().await().map { document ->
                document.toObject(MovierUser::class.java)
            }.first()
        }
    }

    override suspend fun getUserMovies(userId: String): Result<List<MovierItem>, Throwable> {
        return runCatching {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("userId", userId).get().await().map { document ->
                    document.toObject(MovierItem::class.java)
                }
        }
    }

    override suspend fun getMovie(movieId: String): Result<MovierItem, Throwable> {
        return runCatching {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("id", movieId).get().await().map { document ->
                    document.toObject(MovierItem::class.java)
                }.first()
        }
    }
}
