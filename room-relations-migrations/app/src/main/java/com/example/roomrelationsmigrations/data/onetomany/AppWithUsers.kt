package com.example.roomrelationsmigrations.data.onetomany

import androidx.room.Embedded
import androidx.room.Relation

data class AppWithUsers(
    @Embedded val app: AppEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "appId",
    )
    val users: List<UserEntity>,
)
