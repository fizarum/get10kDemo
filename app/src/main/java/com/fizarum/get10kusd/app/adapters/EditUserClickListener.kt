package com.fizarum.get10kusd.app.adapters

import com.fizarum.get10kusd.domain.entities.User

interface EditUserClickListener {
    fun onUserEditInitiated(user: User)
}