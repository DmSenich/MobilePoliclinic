package ru.pin120.policlinic.models

class Specialty(var id:Long?, var name:String?) {
    override fun equals(other: Any?): Boolean {
        other as Specialty
        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }
}