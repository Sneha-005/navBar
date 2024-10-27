package com.example.navbar

import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Services {
    @GET("{username}")
    suspend fun getUserData(@Path("username") username: String): userData

    @GET("{username}/badges")
    suspend fun getUserBadges(@Path("username") username: String): userBadges

    @GET("{username}/solved")
    suspend fun getUserSolved(@Path("username") username: String): userSolved

    @GET("{username}/submission")
    suspend fun getUserSubmissions(@Path("username") username: String ): userSubmission


}

