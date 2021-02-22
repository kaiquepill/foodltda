package singleregistry.domain.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import singleregistry.domain.entities.person.Person

data class Legal(
    @Id
    val id: String? = null,
    val businessName: String?,
    val cnpj: String?,
    val businessType: BusinessType?,
    val person: Person?
)