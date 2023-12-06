package ru.pin120.policlinic.models

import java.util.Date

data class Visiting(
    var id: Long?,
    var doctor: Doctor?,
    var patient: Patient?,
    var date: Date?,
    var diseases: ArrayList<Disease>
)
