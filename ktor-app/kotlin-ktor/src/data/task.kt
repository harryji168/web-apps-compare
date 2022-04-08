package com.moose.data

import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Int, val name: String, val description: String)