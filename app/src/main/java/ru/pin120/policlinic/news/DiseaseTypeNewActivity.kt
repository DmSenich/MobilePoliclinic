package ru.pin120.policlinic.news

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.models.DiseaseType

class DiseaseTypeNewActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_disease_type)
        val btnOK: Button = findViewById(R.id.OK)
        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)

        val etName: EditText = findViewById(R.id.etName)

        btnOK.setOnClickListener {
            val diseaseType = DiseaseType(null, null)
            diseaseType.name = etName.text.toString().trim()
            try {
                diseaseTypeController.addDiseaseType(diseaseType)
                intent.putExtra("isNew", true)
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(
                    this,
                    "Запись создана",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Ошибка при добавлении записи",
                    Toast.LENGTH_LONG
                ).show()
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
