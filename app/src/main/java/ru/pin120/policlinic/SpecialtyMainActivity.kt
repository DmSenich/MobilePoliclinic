package ru.pin120.policlinic

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.SpecialtyAdapter
import ru.pin120.policlinic.models.Specialty
import java.io.IOException
import java.sql.SQLException
import android.widget.ListAdapter as ListAdapter

class SpecialtyMainActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var mDb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_specialties)

        //val textView: TextView = findViewById(R.id.textView)
        mDBHelper = DatabaseHelper(this)
        try{
            mDBHelper.updataDataBase()
        }catch (mIOE:IOException){
            throw Error("UnableToUpdateDatabase")
        }
        try{
            mDb = mDBHelper.writableDatabase
        }catch (mSQLEx: SQLException){
            throw  mSQLEx
        }
        val specialties = ArrayList<Specialty>()
        var specialty : Specialty

        mDb.rawQuery("select * from specialties", null).use{
            cursor ->
            cursor.moveToFirst()

            while (!cursor.isAfterLast){
                specialty = Specialty(null,null)
                specialty.id = cursor.getLong(0)
                specialty.name = cursor.getString(1)
                specialties.add(specialty)
                cursor.moveToNext()
            }
        }

//        cursor.close()
//        val mapSpecialties = Map<Long, String>()

        //val from = arrayOf("id","name")
        //val to = intArrayOf(R.id.tvId,R.id.tvName)
        val adapter = SpecialtyAdapter(this, R.layout.adapter_item_specialties, specialties)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }
}