package ru.pin120.policlinic.news

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.models.Patient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientNewActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var patientController: PatientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_patient)
        val btnOK: Button = findViewById(R.id.btnOK)
        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)

        val etLastName: EditText = findViewById(R.id.etLastName)
        val etFirstName: EditText = findViewById(R.id.etFirstName)
        val etPatr: EditText = findViewById(R.id.etPatr)
        val etDateBirth: DatePicker = findViewById(R.id.etDateBirth)
        val etArea: EditText = findViewById(R.id.etArea)
        val etCity: EditText = findViewById(R.id.etCity)
        val etHouse: EditText = findViewById(R.id.etHouse)
        val etApartment: EditText = findViewById(R.id.etApartment)

        btnOK.setOnClickListener {
            val patient = Patient(
                null, "", "", null, Date(), "", "", "", null
            )
            patient.lastName = etLastName.text.toString().trim()
            patient.firstName = etFirstName.text.toString().trim()
            patient.patr = etPatr.text.toString().trim()
            val year = etDateBirth.year + 1900
            val month = etDateBirth.month
            val day = etDateBirth.dayOfMonth
            val selectedDate = "$year-${month + 1}-$day"
            patient.dateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(selectedDate)!!
            patient.area = etArea.text.toString().trim()
            patient.city = etCity.text.toString().trim()
            patient.house = etHouse.text.toString().trim()
            patient.apartment =  etApartment.text.toString().toLongOrNull()

            try {
                patientController.addPatient(patient)
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Exception of adding record",
                    Toast.LENGTH_SHORT
                ).show()
            }

            intent.putExtra("isNew", true)
            setResult(Activity.RESULT_OK, intent)
            Toast.makeText(
                this,
                "Record created\n Name: ${patient.lastName} ${patient.firstName}",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}

