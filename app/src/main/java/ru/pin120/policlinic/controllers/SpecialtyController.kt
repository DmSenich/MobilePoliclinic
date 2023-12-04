package ru.pin120.policlinic.controllers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.models.Specialty
import java.io.IOException
import java.sql.SQLException

class SpecialtyController(private val mDBHelper: DatabaseHelper) {
    private val mDb: SQLiteDatabase
    init {
        try{
            mDBHelper.updataDataBase()
        }catch (mIOE: IOException){
            throw Error("UnableToUpdateDatabase")
        }
        try{
            mDb = mDBHelper.writableDatabase
        }catch (mSQLEx: SQLException){
            throw  mSQLEx
        }
    }
    fun getAllSpecialties(): List<Specialty> {
        val specialties = ArrayList<Specialty>()
        mDb.rawQuery("SELECT * FROM specialties", null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val specialty = Specialty(null, null)
                specialty.id = cursor.getLong(0)
                specialty.name = cursor.getString(1)
                specialties.add(specialty)
                cursor.moveToNext()
            }
        }
        return specialties
    }
    fun getSpecialtyById(id: Long): Specialty? {
        val cursor = mDb.query(
            "specialties", null, "_id = ?",
            arrayOf(id.toString()), null, null, null
        )
        return if (cursor.moveToFirst()) {
            val specialty = Specialty(null, null)
            specialty.id = cursor.getLong(0)
            specialty.name = cursor.getString(1)
            cursor.close()
            specialty
        } else {
            null
        }
    }
    fun addSpecialty(specialty: Specialty):Long {
        if(specialty.name == "") specialty.name = null
        val values = ContentValues().apply {
            put("name", specialty.name)
        }

        val id = mDb.insert("specialties", null, values)
        if(id == -1L){
            throw Exception("Exception of adding record")
        }
        return id
    }
    fun updateSpecialty(specialty: Specialty) {
        if(specialty.name == "") specialty.name = null
        val values = ContentValues().apply {
            put("name", specialty.name)
        }

        val rowsAffected = mDb.update(
            "specialties", values, "_id = ?",
            arrayOf(specialty.id.toString())
        )
        if(rowsAffected == 0){
            throw Exception("Exception of updating record")
        }
    }
}
