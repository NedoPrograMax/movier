package com.example.themovier.domain.models

data class MovieFromApi(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int,
)