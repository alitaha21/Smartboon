package com.example.smartboon.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.smartboon.activities.MainActivity
import com.example.smartboon.model.User

class Session() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context
    private lateinit var editor: SharedPreferences.Editor
    var ID: Int = 0

    constructor(context: Context) : this() {
        this.context = context
        sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    companion object {
        const val LOGIN: String = "LOGIN"
        const val IS_LOGGED: String = "isLoggedIn"
        const val EMAIL: String = "email"
        const val PASSWORD: String = "password"
        const val NAME: String = "name"
        const val EMAIL_VERIFIED_AT: String = "email_verified_at"
        const val ROLE: String = "role"
        const val REMEMBER_TOKEN: String = "remember_token"
        const val ACTIVATED: String = "activated"
        const val BOON_NUMBER: String = "boon_number"
        const val ROOM_NUMBER: String = "room_number"
    }

    fun createLoginSession(email: String, password: String, user: User) {
        editor.putBoolean(IS_LOGGED, true)
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)
        saveUser(user)
        editor.apply()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED, false)
    }

    fun getUserDetails(): HashMap<String, String> {
        val user: Map<String, String> = HashMap()
        sharedPreferences.getString(NAME, null)?.let { (user as HashMap).put(NAME, it) }
        sharedPreferences.getString(EMAIL, null)?.let { (user as HashMap).put(EMAIL, it) }
        sharedPreferences.getString(EMAIL_VERIFIED_AT, null)?.let { (user as HashMap).put(EMAIL_VERIFIED_AT, it) }
        sharedPreferences.getString(ROLE, null)?.let { (user as HashMap).put(ROLE, it) }
        sharedPreferences.getString(REMEMBER_TOKEN, null)?.let { (user as HashMap).put(REMEMBER_TOKEN, it) }
        sharedPreferences.getInt(ACTIVATED, 0).let { (user as HashMap).put(ACTIVATED, it.toString()) }
        sharedPreferences.getInt(BOON_NUMBER, 0).let { (user as HashMap).put(BOON_NUMBER, it.toString()) }
        sharedPreferences.getInt(ROOM_NUMBER, 0).let { (user as HashMap).put(ROOM_NUMBER, it.toString()) }
        return (user as HashMap)
    }

    private fun saveUser(user: User) {
        sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
        editor.putString(NAME, user.name)
        editor.putString(EMAIL, user.email)
        editor.putString(EMAIL_VERIFIED_AT, user.email_verified_at)
        editor.putString(ROLE, user.role)
        editor.putString(REMEMBER_TOKEN, user.remember_token)
        editor.putInt(ACTIVATED, user.activated!!)
        editor.putInt(BOON_NUMBER, user.boon_number!!)
        editor.putInt(ROOM_NUMBER, user.room_number!!)

        editor.apply()
    }

    fun userId(id: Int) {
        ID = id
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}