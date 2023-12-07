package ru.pin120.policlinic.updates

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.DiseaseTypeAdapter
import ru.pin120.policlinic.adapters.PatientAdapter
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.controllers.PatientController

class DiseaseDiseaseTypeActivity: ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController
    private lateinit var adapter: DiseaseTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list_unbutton)

        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)

        val diseaseTypes = diseaseTypeController.getAllDiseaseTypes()

        adapter = DiseaseTypeAdapter(this, R.layout.adapter_item_disease_types, diseaseTypes)

        val listView: ListView = findViewById(R.id.listView)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            //adapter.toggleSelection(position)
            //val doctorId =
            val diseaseTypeId = view.findViewById<TextView>(R.id.id).text.toString().toLong()
            intent.putExtra("typeId", diseaseTypeId)
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