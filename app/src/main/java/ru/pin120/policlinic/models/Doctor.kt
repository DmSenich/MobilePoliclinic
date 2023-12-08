package ru.pin120.policlinic.models

data class Doctor(
    var id: Long? = null,
    var lastName: String?,
    var firstName: String?,
    var patr: String?,
    var workExp: Int,
    var specialties: List<Specialty> = ArrayList()
)
