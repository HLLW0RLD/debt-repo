package com.example.debt.data.repo

interface UserRepository {

    fun login(email: String, password: String)
    fun register(email: String, password: String)
}