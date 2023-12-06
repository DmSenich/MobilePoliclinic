package ru.pin120.policlinic.updates

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.DoctorAdapter
import ru.pin120.policlinic.adapters.PatientAdapter
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.PatientController

class VisitingPatientActivity: ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var patientController: PatientController
    private lateinit var adapter: PatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_persons)

        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)

        val patients = patientController.getAllPatients()

        adapter = PatientAdapter(this, R.layout.adapter_item_patients, patients)

        val listView: ListView = findViewById(R.id.listView)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            //adapter.toggleSelection(position)
            //val doctorId =
            val patientId = view.findViewById<TextView>(R.id.id).text.toString().toLong()
            intent.putExtra("patientId", patientId)
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