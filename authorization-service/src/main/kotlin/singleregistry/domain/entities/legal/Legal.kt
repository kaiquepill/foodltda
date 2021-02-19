package singleregistry.domain.entities.legal

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import singleregistry.domain.entities.person.Address
import singleregistry.domain.entities.person.Person
import singleregistry.domain.entities.person.PersonType
import singleregistry.domain.entities.person.Phone
import java.time.LocalDateTime

@Document
data class Legal(
    @Id
    val userId: String? = null,
    val businessName: String?,
    val cnpj: String?,
    val businessType: BusinessType,
    val email: String?,

    override val personId: String?,
    override val address: Address?,
    override val phone: Phone,
    override val creationDate: LocalDateTime? = null
) : Person(
    personId,
    address,
    phone,
    PersonType.PJ,
    creationDate
)
