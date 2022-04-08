package com.moose

import com.moose.data.Task
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val tasks = mutableListOf<Task>()

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation){json()}
    routing {
        tasks()
    }

    // Bootstrap our application
    for (i in 0..5)
        tasks.add(Task(i, "Task $i", "Complete doing task $i"))
}

fun Route.tasks() {
    route("/tasks"){
        // Get all Tasks
        get {
            if (tasks.isNotEmpty())
                call.respond(tasks)
            else
                call.respondText("No Tasks available", status = HttpStatusCode.NotFound)
        }

        // Get one Task
        get ("{id}"){
            val id = call.parameters["id"]?.toInt()
            val task = tasks.find { it.id == id }
            if (task == null)
                call.respondText("No task with that id exists", status = HttpStatusCode.NotFound)
            else
                call.respond(task)
        }

        post {
            val task = call.receive<Task>()
            tasks.add(task)
            call.respondText("Task added successfully", status = HttpStatusCode.Accepted)
        }
        delete ("{id}"){
            val id = call.parameters["id"]?.toInt()
            if (tasks.removeIf{it.id == id})
                call.respondText("Task deleted successfully", status = HttpStatusCode.Accepted)
            else
                call.respondText("No task with that id exists", status = HttpStatusCode.NotFound)
        }
    }
}
