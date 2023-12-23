package ru.pin120.policlinic.updates

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.models.DiseaseType

class DiseaseTypeUpdateActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_disease_type)
        val btnOK: Button = findViewById(R.id.OK)
        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val etName: EditText = findViewById(R.id.etName)

        if (intent.extras?.getLong("id") != null) {
            tvId.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id")
            val diseaseType = diseaseTypeController.getDiseaseTypeById(id)
            etName.setText(diseaseType!!.name)
        }
        btnOK.setOnClickListener {
            val diseaseType = DiseaseType(null, null)
            diseaseType.id = tvId.text.toString().toLong()
            diseaseType.name = etName.text.toString().trim()
            try {
                diseaseTypeController.updateDiseaseType(diseaseType)
                Toast.makeText(
                    this,
                    "Запись обновлена",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Ошибка при обновлении записи",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return super.onMenuItemSelected(featureId, item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}


