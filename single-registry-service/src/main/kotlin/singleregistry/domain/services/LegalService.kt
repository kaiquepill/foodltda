package singleregistry.domain.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import singleregistry.domain.entities.legal.Legal
import singleregistry.domain.exceptions.DataValidationException
import singleregistry.domain.exceptions.LegalPersonNotFoundException
import singleregistry.domain.repositories.LegalRepository
import singleregistry.domain.repositories.PersonRepository

@Service
class LegalService(
    private val legalRepository: LegalRepository,
    private val personRepository: PersonRepository
) {
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(LegalService::class.java.name)
    }

    fun create(legal: Legal): Legal {
        validateDate(legal)

        logger.info("Creating legal person")

        personRepository.save(legal.person).also { person ->
            val newLegal = legalRepository.save(legal.copy(person = person))
            logger.info("Legal[${newLegal._id}] with mongo database created")

            return newLegal
        }
    }

    fun findByCnpj(cnpj: String) =
        legalRepository.findByCnpj(cnpj) ?: throw LegalPersonNotFoundException("Person not found by cnpj: $cnpj")

    private fun validateDate(legal: Legal) {
        val error = mutableListOf<String>()

        legal.let {
            if (legalRepository.existsByPersonEmail(it.person.email)) {
                error.add("Email: ${legal.person.email} already use")
            }
        }
        legal.cnpj.let {
            if (legalRepository.existsByCnpj(it)) {
                error.add("CNPJ: ${legal.cnpj} already use")
            }
        }
        legal.person.phone.let {
            if (legalRepository.existsByPersonPhone(it)) {
                error.add(
                    "Phone: ${legal.person.phone?.countryCode}" +
                            "${legal.person.phone?.areaCode}" +
                            "${legal.person.phone?.number} already use"
                )
            }
        }

        if (error.isNotEmpty()) {
            throw DataValidationException(error)
        }
    }
}