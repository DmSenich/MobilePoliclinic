package ru.pin120.policlinic.updates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Doctor

class DoctorUpdateActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_doctor)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val etLastName: EditText = findViewById(R.id.etLastName)
        val etFirstName: EditText = findViewById(R.id.etFirstName)
        val etPatr: EditText = findViewById(R.id.etPatr)
        val etWorkExp: EditText = findViewById(R.id.etWorkExp)
        val btnOK: Button = findViewById(R.id.OK)

        if (intent.extras?.getLong("id") != null) {
            val id = intent.extras!!.getLong("id")
            val doctor = doctorController.getDoctorById(id)
            if (doctor != null) {
                tvId.setText(doctor.id.toString())
                etLastName.setText(doctor.lastName)
                etFirstName.setText(doctor.firstName)
                etPatr.setText(doctor.patr)
                etWorkExp.setText(doctor.workExp.toString())
            }
        }


        btnOK.setOnClickListener {
            val doctor = Doctor(
                id = tvId.text.toString().toLongOrNull(),
                lastName = etLastName.text.toString().trim(),
                firstName = etFirstName.text.toString().trim(),
                patr = etPatr.text.toString().trim(),
                workExp = etWorkExp.text.toString().toIntOrNull() ?: 0
            )
            try {
                doctorController.updateDoctor(doctor)
                Toast.makeText(
                    this,
                    "Record updated\nName: ${doctor.lastName} ${doctor.firstName}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Exception updating record",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

}