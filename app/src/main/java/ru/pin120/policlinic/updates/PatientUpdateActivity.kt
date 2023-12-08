package ru.pin120.policlinic.updates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Patient
import ru.pin120.policlinic.models.Specialty
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PatientUpdateActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var patientController: PatientController
    private var patientId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_patient)
        val btnOK: Button = findViewById(R.id.btnOK)
        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val etLastName: EditText = findViewById(R.id.etLastName)
        val etFirstName: EditText = findViewById(R.id.etFirstName)
        val etPatr: EditText = findViewById(R.id.etPatr)
        val etDateBirth: DatePicker = findViewById(R.id.etDateBirth)
        val etArea: EditText = findViewById(R.id.etArea)
        val etCity: EditText = findViewById(R.id.etCity)
        val etHouse: EditText = findViewById(R.id.etHouse)
        val etApartment: EditText = findViewById(R.id.etApartment)

        if (intent.extras?.getLong("id") != null) {
            patientId = intent.extras?.getLong("id")!!
            tvId.text = patientId.toString()
            val patient = patientController.getPatientById(patientId)
            etLastName.setText(patient!!.lastName)
            etFirstName.setText(patient.firstName)
            etPatr.setText(patient.patr ?: "")
            val calendar = Calendar.getInstance()
            calendar.time = patient.dateBirth
            etDateBirth.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
            )
            etArea.setText(patient.area)
            etCity.setText(patient.city)
            etHouse.setText(patient.house)
            etApartment.setText(patient.apartment.toString() ?: "")
        }

        btnOK.setOnClickListener {
            val patient = Patient(
                null, "", "", null, Date(), "", "", "", null
            )
            patient.id = tvId.text.toString().toLong()
            patient.lastName = etLastName.text.toString().trim()
            patient.firstName = etFirstName.text.toString().trim()
            patient.patr = etPatr.text.toString().trim()
            val year = etDateBirth.year
            val month = etDateBirth.month
            val day = etDateBirth.dayOfMonth
            val selectedDate = "$year-${month + 1}-$day"
            patient.dateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(selectedDate)!!
            patient.area = etArea.text.toString().trim()
            patient.city = etCity.text.toString().trim()
            patient.house = etHouse.text.toString().trim()
            patient.apartment = etApartment.text.toString().toLongOrNull()

            try {
                patientController.updatePatient(patient)
                Toast.makeText(
                    this,
                    "Record updated\n Name: ${patient.lastName} ${patient.firstName}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Exception of updating record",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
    private fun back(){
        val intent = Intent()
        intent.putExtra("id", patientId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        back()
        return true
    }
    override fun onBackPressed() {
        back()
        super.onBackPressed()
    }
}
