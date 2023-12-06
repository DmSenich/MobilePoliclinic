package ru.pin120.policlinic.controllers

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.Patient
import java.io.IOException
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

class PatientController(private val mDBHelper: DatabaseHelper) {
    private val mDb: SQLiteDatabase

    init {
        try {
            mDBHelper.updataDataBase()
        } catch (mIOE: IOException) {
            throw Error("UnableToUpdateDatabase")
        }
        try {
            mDb = mDBHelper.writableDatabase
        } catch (mSQLEx: SQLException) {
            throw mSQLEx
        }
    }

    fun getAllPatients(): List<Patient> {
        val patients = ArrayList<Patient>()
        mDb.rawQuery("SELECT * FROM patients", null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val patient = Patient(null, "", "", null, Date(), "", "", "", null)
                patient.id = cursor.getLong(0)
                patient.lastName = cursor.getString(1)
                patient.firstName = cursor.getString(2)
                patient.patr = cursor.getString(3)
                patient.dateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(cursor.getString(4))!!
                patient.area = cursor.getString(5)
                patient.city = cursor.getString(6)
                patient.house = cursor.getString(7)
                patient.apartment = cursor.getLong(8)
                patients.add(patient)
                cursor.moveToNext()
            }
        }
        return patients
    }

    fun getPatientById(id: Long): Patient? {
        val cursor = mDb.query(
            "patients", null, "_id = ?",
            arrayOf(id.toString()), null, null, null
        )
        return if (cursor.moveToFirst()) {
            val patient = Patient(null, "", "", null, Date(), "", "", "", null)
            patient.id = cursor.getLong(0)
            patient.lastName = cursor.getString(1)
            patient.firstName = cursor.getString(2)
            patient.patr = cursor.getString(3)
            patient.dateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(cursor.getString(4))!!
            patient.area = cursor.getString(5)
            patient.city = cursor.getString(6)
            patient.house = cursor.getString(7)
            patient.apartment = cursor.getLong(8)
            cursor.close()
            patient
        } else {
            null
        }
    }

    fun addPatient(patient: Patient): Long {
        if (patient.patr == "") patient.patr = null
        if(patient.apartment == 0L) patient.apartment = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val values = ContentValues().apply {
            put("lastname", patient.lastName)
            put("firstname", patient.firstName)
            put("patr", patient.patr)
            put("datebirth", dateFormat.format(patient.dateBirth))
            put("area", patient.area)
            put("city", patient.city)
            put("house", patient.house)
            put("apartment", patient.apartment)
        }

        val id = mDb.insert("patients", null, values)
        if (id == -1L) {
            throw Exception("Exception of adding record")
        }
        return id
    }

    fun updatePatient(patient: Patient) {
        if (patient.patr == "") patient.patr = null
        if(patient.apartment == 0L) patient.apartment = null
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val values = ContentValues().apply {
            put("lastname", patient.lastName)
            put("firstname", patient.firstName)
            put("patr", patient.patr)
            put("datebirth", dateFormat.format(patient.dateBirth))
            put("area", patient.area)
            put("city", patient.city)
            put("house", patient.house)
            put("apartment", patient.apartment)
        }

        val rowsAffected = mDb.update(
            "patients", values, "_id = ?",
            arrayOf(patient.id.toString())
        )
        if (rowsAffected == 0) {
            throw Exception("Exception of updating record")
        }
    }
}
