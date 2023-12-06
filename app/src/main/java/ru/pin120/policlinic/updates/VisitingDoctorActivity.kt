package ru.pin120.policlinic.updates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.DoctorAdapter
import ru.pin120.policlinic.controllers.DoctorController

class VisitingDoctorActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController
    private lateinit var adapter: DoctorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_persons)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)

        val doctors = doctorController.getAllDoctors()

        adapter = DoctorAdapter(this, R.layout.adapter_item_doctors, doctors)

        val listView: ListView = findViewById(R.id.listView)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            //adapter.toggleSelection(position)
            //val doctorId =
            val doctorId = view.findViewById<TextView>(R.id.id).text.toString().toLong()
            val intent = Intent()
            intent.putExtra("doctorId", doctorId)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

//        listView.setOnItemLongClickListener { _, _, position, _ ->
//            adapter.toggleSelection(position)
//            true
//        }
    }

//    override fun onBackPressed() {
//        val selectedDoctorIds = adapter.getSelectedDoctors().mapNotNull { it.id }.toLongArray()
//        val resultIntent = Intent()
//        resultIntent.putExtra("selectedDoctorIds", selectedDoctorIds)
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
//    }
}