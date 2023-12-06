package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Specialty
import ru.pin120.policlinic.updates.PatientUpdateActivity
import ru.pin120.policlinic.updates.SpecialtyUpdateActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PatientDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var patientController: PatientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_patient)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val tvLastName: TextView = findViewById(R.id.tvLastName)
        val tvFirstName: TextView = findViewById(R.id.tvFirstName)
        val tvPatr: TextView = findViewById(R.id.tvPatr)
        val tvDateBirth: TextView = findViewById(R.id.tvDateBirth)
        val tvArea: TextView = findViewById(R.id.tvArea)
        val tvCity: TextView = findViewById(R.id.tvCity)
        val tvHouse: TextView = findViewById(R.id.tvHouse)
        val tvApartment: TextView = findViewById(R.id.tvApartment)

        setPatientView(tvId, tvLastName, tvFirstName, tvPatr, tvDateBirth, tvArea, tvCity, tvHouse, tvApartment)

        btnUpdate.setOnClickListener {
            val intent = Intent(this@PatientDetailsActivity, PatientUpdateActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tvId: TextView = findViewById(R.id.tvId)
        val tvLastName: TextView = findViewById(R.id.tvLastName)
        val tvFirstName: TextView = findViewById(R.id.tvFirstName)
        val tvPatr: TextView = findViewById(R.id.tvPatr)
        val tvDateBirth: TextView = findViewById(R.id.tvDateBirth)
        val tvArea: TextView = findViewById(R.id.tvArea)
        val tvCity: TextView = findViewById(R.id.tvCity)
        val tvHouse: TextView = findViewById(R.id.tvHouse)
        val tvApartment: TextView = findViewById(R.id.tvApartment)

        if (resultCode == Activity.RESULT_OK) {
            setPatientView(tvId, tvLastName, tvFirstName, tvPatr, tvDateBirth, tvArea, tvCity, tvHouse, tvApartment)
        }
    }

    private fun setPatientView(
        tvId: TextView,
        tvLastName: TextView,
        tvFirstName: TextView,
        tvPatr: TextView,
        tvDateBirth: TextView,
        tvArea: TextView,
        tvCity: TextView,
        tvHouse: TextView,
        tvApartment: TextView
    ) {
        if (intent.extras?.getLong("id") != null) {
            tvId.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id")
            val patient = patientController.getPatientById(id)
            tvLastName.text = patient!!.lastName
            tvFirstName.text = patient.firstName
            tvPatr.text = patient.patr ?: ""

            tvDateBirth.text =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(patient.dateBirth)
            tvArea.text = patient.area
            tvCity.text = patient.city
            tvHouse.text = patient.house
            if(patient.apartment == 0L){
                tvApartment.text = ""
            }
            else{
                tvApartment.text = patient.apartment.toString()
            }
        }
    }
}
