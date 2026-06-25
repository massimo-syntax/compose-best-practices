package com.example.data.repository

import com.example.domain.model.Item
import com.example.domain.repository.ItemsRepository
import jakarta.inject.Inject

class ItemsRepositoryImpl @Inject constructor() : ItemsRepository {
    override suspend fun getItems(): List<Item> {
        return listOf(
            Item("123","3that is the text"),
            Item("124","4that is the text"),
            Item("125","5that is the text"),
            Item("126","6that is the text"),
            Item("127","7that is the text"),
            )
    }
}