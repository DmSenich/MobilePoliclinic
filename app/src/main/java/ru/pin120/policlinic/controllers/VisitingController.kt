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
import java.util.Date
import java.util.Locale
import kotlin.math.max

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
    fun getVisitingsByDoctorIdAndPatientId(doctorId: Long, patientId: Long, minDate:Date?, maxDate:Date?): List<Visiting> {
        val visitings = ArrayList<Visiting>()
        if(!(doctorId == 0L && patientId == 0L)){
            var queryBuilder = "SELECT * FROM visitings where "
            if(doctorId != 0L){
                queryBuilder += " _iddoctor = ${doctorId}"
                if(patientId != 0L){
                    queryBuilder += " and _idpatient = ${patientId}"
                }
            }
            else if(patientId != 0L){
                queryBuilder += " _idpatient = ${patientId}"
            }
            val query = queryBuilder
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
        }
        else{
            visitings.addAll(getAllVisitings())
        }

        val filter:List<Visiting>
        if(minDate != null && maxDate != null){
            filter = visitings.filter { !it.date!!.before(minDate) && !it.date!!.after(maxDate) }
            return filter
        }
        else{
            if(minDate != null){
                filter = visitings.filter { !it.date!!.before(minDate)  }
                return filter
            }
            else if(maxDate != null){
                filter = visitings.filter { !it.date!!.after(maxDate) }
                return filter
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
//        val diseaseController = DiseaseController(dbHelper)
//        val diseases = diseaseController.getAllDiseasesForVisiting(visitingId)
//        return diseases
        return ArrayList()
    }
    public fun deleteVisiting(visitingId:Long){

        val query = "select count(*) from diseases where _idvisiting = ?"
        val selectionArgs = arrayOf(visitingId.toString())
        val countDiseases = mDb.rawQuery(query, selectionArgs).use {cursor ->
            cursor.moveToFirst()
            cursor.getLong(0)
        }

        if(countDiseases == 0L){
            mDb.delete("visitings", "_id = ?", selectionArgs)
        }
        else{
            throw SQLException("Failed to delete visiting")
        }
    }

}
