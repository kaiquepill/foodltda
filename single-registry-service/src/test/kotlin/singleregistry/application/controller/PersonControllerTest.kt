package singleregistry.application.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import singleregistry.application.controllers.PersonController
import singleregistry.domain.entities.person.PersonType
import singleregistry.domain.services.PersonService
import singleregistry.factories.PersonFactory

class PersonControllerTest {
    private lateinit var personService: PersonService
    private lateinit var personController: PersonController

    @BeforeEach
    fun beforeEach() {
        personService =  mockk(relaxed = true)
        personController = PersonController(personService)
    }

    @Test
    fun `given valid person id should return person and http status 200`() {
        val person = PersonFactory.sample(personType = PersonType.PJ)
        val personId = "001"

        every { personService.findByPersonId(any()) } returns person

        val controller = personController.getByPersonId(personId)

        verify { personService.findByPersonId(any()) }
        Assertions.assertEquals(HttpStatus.ACCEPTED, controller.statusCode)
    }
}