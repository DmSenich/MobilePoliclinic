package ru.pin120.policlinic.controllers

import android.content.ContentValues
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.Disease
import ru.pin120.policlinic.models.DiseaseType
import ru.pin120.policlinic.models.Visiting

class DiseaseController(private val dbHelper: DatabaseHelper) {

    private val mDb: SQLiteDatabase = dbHelper.writableDatabase
    private val visitingController = VisitingController(dbHelper)
    private val diseaseTypeController = DiseaseTypeController(dbHelper)

    fun getAllDiseasesForVisiting(visitingId: Long): List<Disease> {
        val diseases = ArrayList<Disease>()

        val query = "SELECT * FROM diseases WHERE _idvisiting = ?"
        val selectionArgs = arrayOf(visitingId.toString())

        val cursor = mDb.rawQuery(query, selectionArgs).use{cursor ->
            while (!cursor.isAfterLast) {
                val id = cursor.getLong(0)
                val diseaseTypeId = cursor.getLong(1)
                val description = cursor.getString(3)

                val diseaseType = getTypeById(diseaseTypeId)
                val visiting = getVisitingById(visitingId)
                val disease = Disease(
                    id = id,
                    diseaseType = diseaseType,
                    description = description,
                    visiting = visiting
                )
                diseases.add(disease)
                cursor.moveToNext()
            }
        }
        return diseases
    }

    private fun getVisitingById(visitingId: Long): Visiting? {
        return visitingController.getVisitingById(visitingId)
    }

    private fun getTypeById(diseaseTypeId: Long): DiseaseType? {
        return diseaseTypeController.getDiseaseTypeById(diseaseTypeId)
    }

    fun addDisease(disease: Disease): Long {
        val values = ContentValues().apply {
            put("_idvisiting", disease.visiting?.id)
            put("_iddiseasetype", disease.diseaseType!!.id)
            put("description", disease.description)
        }
        val id = mDb.insert("diseases", null, values)
        if(id == -1L){
            throw SQLException("Failed to insert disease")
        }
        return id
    }

    fun updateDisease(disease: Disease) {
        val values = ContentValues().apply {
            put("_iddiseasetype", disease.diseaseType!!.id)
            put("description", disease.description)
        }

        val rowsAffected = mDb.update("disease", values, "_id = ?", arrayOf(disease.id.toString()))
        if(rowsAffected == 0){
            throw SQLException("Failed to update disease")
        }
    }

//    fun deleteDisease(diseaseId: Long) {
//        val selection = "${DatabaseContract.DiseaseEntry._ID} = ?"
//        val selectionArgs = arrayOf(diseaseId.toString())
//
//        mDb.delete(DatabaseContract.DiseaseEntry.TABLE_NAME, selection, selectionArgs)
//    }
}