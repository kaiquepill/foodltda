package singleregistry.application.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import singleregistry.application.controllers.LegalController
import singleregistry.application.dto.toDomain
import singleregistry.domain.entities.legal.Legal
import singleregistry.domain.exceptions.ResultBindingException
import singleregistry.domain.services.LegalService
import singleregistry.factories.LegalFactory
import singleregistry.factories.LegalRequestFactory

class LegalControllerTest {
    private lateinit var legalService: LegalService
    private lateinit var legalController: LegalController
    private lateinit var result: BindingResult

    @BeforeEach
    fun beforeEach() {
        legalService =  mockk(relaxed = true)
        legalController = LegalController(legalService)
        result = mockk(relaxed = true)
    }

    @Test
    fun `given valid legal request should return legal and http status 201`() {
        val request = LegalRequestFactory.sample()
        val legal = LegalFactory.sample()

        every { legalService.create(request.toDomain(), request.password) } returns legal

        val controller = legalController.register(request, result)

        verify { legalService.create(any(), any()) }
        Assertions.assertEquals(HttpStatus.CREATED, controller.statusCode)
    }

    @Test
    fun `given invalid legal request should return a resultBindingException`() {
        val request = LegalRequestFactory.sample()

        every { result.hasErrors() } returns true

        assertThrows<ResultBindingException> {
            legalController.register(request, result)
        }
    }

    @Test
    fun `given valid cnpj request should return a legal person`() {
        val legal = LegalFactory.sample()
        val cnpj = "10.501.210/0001-17"

        every { legalService.findByCnpj(any()) } returns legal

        val controller = legalController.getByCnpj(cnpj)

        verify { legalService.findByCnpj(cnpj) }
        Assertions.assertEquals(HttpStatus.ACCEPTED, controller.statusCode)
        Assertions.assertEquals(legal, controller.body)
    }
}