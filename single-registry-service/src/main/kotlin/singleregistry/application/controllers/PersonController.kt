package singleregistry.application.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import singleregistry.domain.services.PersonService

@RestController
@RequestMapping("/person")
class PersonController(private val personService: PersonService){

    @GetMapping
    fun getByToken(): ResponseEntity<Any> {

        return ResponseEntity<Any>(personService.findByToken(), HttpStatus.ACCEPTED)
    }

    @GetMapping("/{personId}")
    fun getByPersonId(@PathVariable personId: String): ResponseEntity<Any> {
        return ResponseEntity<Any>(personService.findByPersonId(personId), HttpStatus.ACCEPTED)
    }
}