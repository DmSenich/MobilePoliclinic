package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.SpecialtyAdapter
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.details.SpecialtyDetailsActivity
import ru.pin120.policlinic.news.SpecialtyNewActivity

class SpecialtyMainActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        //val textView: TextView = findViewById(R.id.textView)
        mDBHelper = DatabaseHelper(this)
        specialtyController = SpecialtyController(mDBHelper)
        val specialties = specialtyController.getAllSpecialties()

        val adapter = SpecialtyAdapter(this, R.layout.adapter_item_specialties, specialties)
        val listView: ListView = findViewById(R.id.listView)
        val btnNew:Button = findViewById(R.id.bNew)
        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->
            val idTV : TextView = view.findViewById(R.id.id)
            val nameTV:TextView = view.findViewById(R.id.name)
            val selectedId = idTV.text.toString().toLong()
            val selectedName = nameTV.text.toString()
            val intent = Intent(this@SpecialtyMainActivity, SpecialtyDetailsActivity::class.java)
            intent.putExtra("id", selectedId)
            startActivity(intent)
//            Toast.makeText(
//                this,
//                "Selected ID: $selectedId, Name: $selectedName",
//                Toast.LENGTH_SHORT
//            ).show()


        }
        btnNew.setOnClickListener {
            val intent = Intent(this@SpecialtyMainActivity, SpecialtyNewActivity::class.java)
            startActivityForResult(intent, 0)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val specialties = specialtyController.getAllSpecialties()
        val adapter = SpecialtyAdapter(this, R.layout.adapter_item_specialties, specialties)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
//    private fun getAllSpecialties() : ArrayList<Specialty>{
////        val specialties = ArrayList<Specialty>()
////        var specialty : Specialty
////
////        //specialties = mDBHelper.getSpecialties();
////        mDb.rawQuery("select * from specialties", null).use{
////                cursor ->
////            cursor.moveToFirst()
////
////            while (!cursor.isAfterLast){
////                specialty = Specialty(null,null)
////                specialty.id = cursor.getLong(0)
////                specialty.name = cursor.getString(1)
////                specialties.add(specialty)
////                cursor.moveToNext()
////            }
////        }
//        return mDBHelper.getSpecialties()
//    }
}