package com.example.navbar

data class userBadges(
    val activeBadge: Any?,
    val badges: List<Any>?,
    val badgesCount: Int?,
    val upcomingBadges: List<UpcomingBadge>?
)