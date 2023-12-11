package ru.pin120.policlinic.controllers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.DiseaseType
import java.io.IOException
import java.sql.SQLException

class DiseaseTypeController(private val mDBHelper: DatabaseHelper) {
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

    fun getAllDiseaseTypes(): List<DiseaseType> {
        val diseaseTypes = ArrayList<DiseaseType>()
        mDb.rawQuery("SELECT * FROM diseasetypes", null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val diseaseType = DiseaseType(null, null)
                diseaseType.id = cursor.getLong(0)
                diseaseType.name = cursor.getString(1)
                diseaseTypes.add(diseaseType)
                cursor.moveToNext()
            }
        }
        return diseaseTypes
    }

    fun getDiseaseTypeById(id: Long): DiseaseType? {
        val cursor = mDb.query(
            "diseasetypes", null, "_id = ?",
            arrayOf(id.toString()), null, null, null
        )
        return if (cursor.moveToFirst()) {
            val diseaseType = DiseaseType(null, null)
            diseaseType.id = cursor.getLong(0)
            diseaseType.name = cursor.getString(1)
            cursor.close()
            diseaseType
        } else {
            null
        }
    }

    fun addDiseaseType(diseaseType: DiseaseType): Long {
        if (diseaseType.name == "") diseaseType.name = null
        val values = ContentValues().apply {
            put("name", diseaseType.name)
        }

        val id = mDb.insert("diseasetypes", null, values)
        if (id == -1L) {
            throw Exception("Exception of adding record")
        }
        return id
    }

    fun updateDiseaseType(diseaseType: DiseaseType) {
        if (diseaseType.name == "") diseaseType.name = null
        val values = ContentValues().apply {
            put("name", diseaseType.name)
        }

        val rowsAffected = mDb.update(
            "diseasetypes", values, "_id = ?",
            arrayOf(diseaseType.id.toString())
        )
        if (rowsAffected == 0) {
            throw Exception("Exception of updating record")
        }
    }
    fun deleteDiseaseType(diseaseTypeId:Long){
        val query = "select count(*) from diseases where _iddiseasetype = ?"
        val selectionArgs = arrayOf(diseaseTypeId.toString())
        val countDiseases = mDb.rawQuery(query, selectionArgs).use {cursor ->
            cursor.moveToFirst()
            cursor.getLong(0)
        }

        if(countDiseases == 0L){
            mDb.delete("diseasetypes", "_id = ?", selectionArgs)
        }
        else{
            throw android.database.SQLException("Failed to delete disease_type")
        }
    }
}
