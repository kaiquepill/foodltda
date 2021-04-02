package singleregistry.application.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import singleregistry.domain.services.PersonService

@RestController
@RequestMapping("/person")
class PersonController(private val personService: PersonService){

    //auth token
    @GetMapping("/{personId}")
    fun getByPersonId(@PathVariable personId: String): ResponseEntity<Any> {

        return ResponseEntity<Any>(personService.findByPersonId(), HttpStatus.ACCEPTED)
    }
}