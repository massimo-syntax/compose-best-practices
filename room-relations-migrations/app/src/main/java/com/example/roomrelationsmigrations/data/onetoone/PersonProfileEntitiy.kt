package com.example.roomrelationsmigrations.data.onetoone

import androidx.room.Embedded
import androidx.room.Relation

data class PersonProfileEntity(
    @Embedded val person: PersonEntity,
    @Relation(
        // its id
        parentColumn = "id",
        // related entity id
        entityColumn = "personId"
    )
    val profile: ProfileEntity?
)
