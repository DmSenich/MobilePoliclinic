package ru.pin120.policlinic.controllers

import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.Disease
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.models.Patient
import ru.pin120.policlinic.models.Visiting
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingController(private val dbHelper: DatabaseHelper) {

    private val mDb: SQLiteDatabase = dbHelper.writableDatabase
    private val doctorController = DoctorController(dbHelper)
    private val patientController = PatientController(dbHelper)

    fun getAllVisitings(): List<Visiting> {
        val visitings = ArrayList<Visiting>()
        val query = "SELECT * FROM visitings"
        mDb.rawQuery(query, null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val visiting = Visiting(
                    id = cursor.getLong(0),
                    doctor = getDoctorById(cursor.getLong(1)),
                    patient = getPatientById(cursor.getLong(2)),
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(cursor.getString(3)),
                    diseases = getDiseasesForVisiting(cursor.getLong(0))
                )
                visitings.add(visiting)
                cursor.moveToNext()
            }
        }
        return visitings
    }

    fun getVisitingById(id: Long): Visiting? {
        val cursor = mDb.query(
            "visitings", null, "_id = ?",
            arrayOf(id.toString()), null, null, null
        )
        return if (cursor.moveToFirst()) {
            val visiting = Visiting(
                id = cursor.getLong(0),
                doctor = getDoctorById(cursor.getLong(1)),
                patient = getPatientById(cursor.getLong(2)),
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(cursor.getString(3)),
                diseases = getDiseasesForVisiting(cursor.getLong(0))
            )
            cursor.close()
            visiting
        } else {
            null
        }
    }

    fun addVisiting(visiting: Visiting): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val values = ContentValues().apply {
            put("_iddoctor", visiting.doctor?.id)
            put("_idpatient", visiting.patient?.id)
            put("date", dateFormat.format(visiting.date))
        }

        val id = mDb.insert("visitings", null, values)
        if (id == -1L) {
            throw SQLException("Failed to insert visiting")
        }

        return id
    }

    fun updateVisiting(visiting: Visiting) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val values = ContentValues().apply {
            put("_iddoctor", visiting.doctor?.id)
            put("_idpatient", visiting.patient?.id)
            put("date", dateFormat.format(visiting.date))
        }
        val rowsAffected = mDb.update("visitings", values, "_id = ?", arrayOf(visiting.id.toString()))
        if (rowsAffected == 0) {
            throw SQLException("Failed to update visiting")
        }
    }

    private fun getDoctorById(doctorId: Long): Doctor? {
        return doctorController.getDoctorById(doctorId)
    }

    private fun getPatientById(patientId: Long): Patient? {
        return patientController.getPatientById(patientId)
    }

    private fun getDiseasesForVisiting(visitingId: Long): ArrayList<Disease> {
        return  ArrayList()
    }

}
