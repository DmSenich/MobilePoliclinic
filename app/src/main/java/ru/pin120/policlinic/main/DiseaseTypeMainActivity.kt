package ru.pin120.policlinic.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.DiseaseTypeAdapter
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.details.DiseaseTypeDetailsActivity
import ru.pin120.policlinic.news.DiseaseTypeNewActivity

class DiseaseTypeMainActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)
        val diseaseTypes = diseaseTypeController.getAllDiseaseTypes()

        val adapter = DiseaseTypeAdapter(this, R.layout.adapter_item_disease_types, diseaseTypes)
        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val idTV: TextView = view.findViewById(R.id.id)
            val nameTV: TextView = view.findViewById(R.id.name)
            val selectedId = idTV.text.toString().toLong()
            val selectedName = nameTV.text.toString()
            val intent = Intent(this@DiseaseTypeMainActivity, DiseaseTypeDetailsActivity::class.java)
            intent.putExtra("id", selectedId)
            startActivityForResult(intent, 0)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@DiseaseTypeMainActivity, DiseaseTypeNewActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val diseaseTypes = diseaseTypeController.getAllDiseaseTypes()

            val adapter = DiseaseTypeAdapter(this, R.layout.adapter_item_disease_types, diseaseTypes)
            val listView: ListView = findViewById(R.id.listView)
            listView.adapter = adapter
        }
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
}
