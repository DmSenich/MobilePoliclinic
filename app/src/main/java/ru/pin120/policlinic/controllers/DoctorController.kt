package ru.pin120.policlinic.controllers

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.models.Specialty

class DoctorController(private val dbHelper: DatabaseHelper) {

    private val mDb: SQLiteDatabase = dbHelper.writableDatabase

    fun getAllDoctors(): List<Doctor> {
        val doctors = ArrayList<Doctor>()
        val query = "SELECT * FROM doctors"
        mDb.rawQuery(query, null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val doctor = Doctor(
                    id = cursor.getLong(0),
                    lastName = cursor.getString(1),
                    firstName = cursor.getString(2),
                    patr = cursor.getString(3),
                    workExp = cursor.getInt(4),
                    specialties = getDoctorSpecialties(cursor.getLong(0))
                )
                doctors.add(doctor)
                cursor.moveToNext()
            }
        }
        return doctors
    }

    fun getDoctorById(id: Long): Doctor? {
        val cursor = mDb.query(
        "doctors", null, "_id = ?",
        arrayOf(id.toString()), null, null, null
        )
        return if(cursor.moveToFirst()){

            val doctor = Doctor(
                id = cursor.getLong(0),
                lastName = cursor.getString(1),
                firstName = cursor.getString(2),
                patr = cursor.getString(3),
                workExp = cursor.getInt(4),
                specialties = getDoctorSpecialties(cursor.getLong(0))
            )
            cursor.close()
            doctor
        }else
             null
    }


    fun addDoctor(doctor: Doctor): Long {
        val values = ContentValues().apply {
            put("lastname", doctor.lastName)
            put("firstname", doctor.firstName)
            put("patr", doctor.patr)
            put("workexp", doctor.workExp)
        }

        val id = mDb.insert("doctors", null, values)
        if (id == -1L) {
            throw SQLException("Failed to insert doctor")
        }

        return id
    }

    fun updateDoctor(doctor: Doctor) {
        val values = ContentValues().apply {
            put("lastname", doctor.lastName)
            put("firstname", doctor.firstName)
            put("patr", doctor.patr)
            put("workexp", doctor.workExp)
        }
        val rowsAffected = mDb.update("doctors", values, "_id = ?", arrayOf(doctor.id.toString()))
        if (rowsAffected == 0) {
            throw SQLException("Failed to update doctor")
        }
    }

    private fun addDoctorSpecialties(doctorId: Long, specialties: List<Specialty>) {
        for (specialty in specialties) {
            val values = ContentValues().apply {
                put("_iddoctor", doctorId)
                put("_idspecialty", specialty.id)
            }
            mDb.insert("doctors_specialties", null, values)
        }
    }

    fun updateDoctorSpecialties(doctorId: Long, specialties: List<Specialty>) {
        mDb.delete("doctors_specialties", "_iddoctor = ?", arrayOf(doctorId.toString()))
        addDoctorSpecialties(doctorId, specialties)
    }

    fun getDoctorSpecialties(doctorId: Long): List<Specialty> {
        val specialties = ArrayList<Specialty>()
        val query = "SELECT _idspecialty FROM doctors_specialties WHERE _iddoctor = ?"
        mDb.rawQuery(query, arrayOf(doctorId.toString())).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val specialtyId = cursor.getLong(0)
                val specialty = getSpecialtyById(specialtyId)
                if (specialty != null) {
                    specialties.add(specialty)
                }
                cursor.moveToNext()
            }
        }
        return specialties
    }

    private fun getSpecialtyById(specialtyId: Long): Specialty? {
        val query = "SELECT * FROM specialties WHERE _id = ?"
        val cursor = mDb.rawQuery(query, arrayOf(specialtyId.toString()))
        return try {
            if (cursor.moveToFirst()) {
                Specialty(
                    id = cursor.getLong(0),
                    name = cursor.getString(1)
                )
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }
    fun getDoctorsBySpecialtiesId(specialtiesId: ArrayList<Long>) : List<Doctor>{
        val doctors = ArrayList<Doctor>()
        val placeholders = specialtiesId.joinToString(",") { "?" }
        val selectionArgs = specialtiesId.map { it.toString() }.toTypedArray()
        val query = "SELECT _iddoctor FROM doctors_specialties WHERE _idspecialty IN ($placeholders)"
        mDb.rawQuery(query, selectionArgs).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val doctorId = cursor.getLong(0)
                val doctor = getDoctorById(doctorId)
                if(doctor != null)
                    doctors.add(doctor)
                cursor.moveToNext()
            }
        }
        return doctors
    }
    fun deleteDoctor(doctorId:Long){
        val query = "select count(*) from visitings where _iddoctor = ?"
        val selectionArgs = arrayOf(doctorId.toString())
        val countVisitings = mDb.rawQuery(query, selectionArgs).use {cursor ->
            cursor.moveToFirst()
            cursor.getLong(0)
        }
        if(countVisitings == 0L){
            updateDoctorSpecialties(doctorId, ArrayList())
            mDb.delete("doctors", "_id = ?", selectionArgs)
        }
        else{
            throw android.database.SQLException("Failed to delete patient")
        }
    }
}