package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.updates.DiseaseTypeUpdateActivity

class DiseaseTypeDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController
    private var diseaseTypeId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_disease_type)
        val btnUpdate: Button = findViewById(R.id.bUpdate)
        val btnDelete:Button = findViewById(R.id.bDelete)
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
        btnDelete.setOnClickListener {
            try {
                diseaseTypeController.deleteDiseaseType(diseaseTypeId)
                val intent =Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            catch (ex:Exception){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
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
            diseaseTypeId = intent.extras?.getLong("id")!!
            tvId.text = diseaseTypeId.toString()
            val diseaseType = diseaseTypeController.getDiseaseTypeById(diseaseTypeId)
            tvName.text = diseaseType!!.name
        }
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        setResult(RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }

}
