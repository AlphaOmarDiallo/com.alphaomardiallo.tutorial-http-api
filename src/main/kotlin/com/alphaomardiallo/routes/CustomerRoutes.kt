package com.alphaomardiallo.routes

import com.alphaomardiallo.models.Customer
import com.alphaomardiallo.models.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText(text = "No customer found", status = HttpStatusCode.OK)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer = customerStorage.find { it.id == id } ?: return@get call.respondText(
                text = "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText(text = "Customer stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf{it.id == id}) {
                call.respondText(text = "Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText(text = "Customer to delete not found", status = HttpStatusCode.NotFound)
            }

        }
    }
}