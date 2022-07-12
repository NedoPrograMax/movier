package com.example.themovier.data.repo

import com.example.themovier.data.models.MovierUserModel
import com.example.themovier.domain.models.MovierItemModel
import com.example.themovier.domain.repositories.FireRepo
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepoImpl @Inject constructor() : FireRepo {
    private val queryUser = FirebaseFirestore.getInstance().collection("users")
    override suspend fun getUserInfo(userId: String): Result<MovierUserModel, Throwable> {
        return runCatching {
            queryUser.whereEqualTo("userId", userId).get().await().map { document ->
                document.toObject(MovierUserModel::class.java)
            }.first()
        }
    }

    override suspend fun getUserMovies(userId: String): Result<List<MovierItemModel>, Throwable> {
        return runCatching {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("userId", userId).get().await().map { document ->
                    document.toObject(MovierItemModel::class.java)
                }
        }
    }

    override suspend fun getMovie(movieId: String): Result<MovierItemModel, Throwable> {
        return runCatching {
            FirebaseFirestore.getInstance().collection("movies")
                .whereEqualTo("id", movieId).get().await().map { document ->
                    document.toObject(MovierItemModel::class.java)
                }.first()
        }
    }
}
