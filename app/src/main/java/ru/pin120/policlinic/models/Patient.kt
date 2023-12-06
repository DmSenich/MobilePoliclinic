package ru.pin120.policlinic.models

import java.util.Date

class Patient(
    var id: Long?,
    var lastName: String,
    var firstName: String,
    var patr: String?,
    var dateBirth: Date,
    var area: String,
    var city: String,
    var house: String,
    var apartment: Long?
)