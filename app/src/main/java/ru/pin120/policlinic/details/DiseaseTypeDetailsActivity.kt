package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.updates.DiseaseTypeUpdateActivity

class DiseaseTypeDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_disease_type)
        val btnUpdate: Button = findViewById(R.id.bUpdate)
        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val tvName: TextView = findViewById(R.id.etName)
        setDiseaseTypeView(tvId, tvName)

        btnUpdate.setOnClickListener {
            val intent = Intent(this@DiseaseTypeDetailsActivity, DiseaseTypeUpdateActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tvId: TextView = findViewById(R.id.tvId)
        val tvName: TextView = findViewById(R.id.etName)
        if (resultCode == Activity.RESULT_OK) {
            setDiseaseTypeView(tvId, tvName)
        }
    }

    private fun setDiseaseTypeView(tvId: TextView, tvName: TextView) {
        if (intent.extras?.getLong("id") != null) {
            tvId.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id");
            val diseaseType = diseaseTypeController.getDiseaseTypeById(id)
            tvName.text = diseaseType!!.name
        }
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return super.onMenuItemSelected(featureId, item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
