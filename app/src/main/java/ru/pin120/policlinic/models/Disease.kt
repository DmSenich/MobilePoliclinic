package ru.pin120.policlinic.models

data class Disease(
    var id: Long?,
    var diseaseType: DiseaseType?,
    var description: String?,
    var visiting: Visiting?
)