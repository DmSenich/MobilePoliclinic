package ru.pin120.policlinic.news

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
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
                null, null, null, null, Date(), null, null, null, null
            )
            var lastName:String? = etLastName.text.toString().trim()
            var firstName:String? = etFirstName.text.toString().trim()
            var patr:String? = etPatr.text.toString().trim()
            var area:String? = etArea.text.toString().trim()
            var city:String? = etCity.text.toString().trim()
            var house:String? = etHouse.text.toString().trim()
            if (lastName == "") lastName = null
            if (firstName == "") firstName = null
            if (patr == "") patr = null
            if (area == "") area = null
            if (city == "") city = null
            if (house == "") house = null
            patient.lastName = lastName
            patient.firstName = firstName
            patient.patr = patr
            val year = etDateBirth.year + 1900
            val month = etDateBirth.month
            val day = etDateBirth.dayOfMonth
            val selectedDate = "$year-${month + 1}-$day"
            patient.dateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(selectedDate)!!
            patient.area = area
            patient.city = city
            patient.house = house
            patient.apartment =  etApartment.text.toString().toLongOrNull()

            try {
                patientController.addPatient(patient)
                intent.putExtra("isNew", true)
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(
                    this,
                    "Record created\n Name: ${patient.lastName} ${patient.firstName}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Exception of adding record",
                    Toast.LENGTH_SHORT
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

