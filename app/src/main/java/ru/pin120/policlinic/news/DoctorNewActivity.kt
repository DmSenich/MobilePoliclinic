package ru.pin120.policlinic.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.models.Patient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DoctorNewActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_doctor)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)

        val etLastName: EditText = findViewById(R.id.etLastName)
        val etFirstName: EditText = findViewById(R.id.etFirstName)
        val etPatr: EditText = findViewById(R.id.etPatr)
        val etWorkExp: EditText = findViewById(R.id.etWorkExp)
        val btnOK: Button = findViewById(R.id.OK)


        btnOK.setOnClickListener {
            val doctor = Doctor(
                lastName = etLastName.text.toString().trim(),
                firstName = etFirstName.text.toString().trim(),
                patr = etPatr.text.toString().trim(),
                workExp = etWorkExp.text.toString().toIntOrNull() ?: 0)
            try {
                doctorController.addDoctor(doctor)
                Toast.makeText(
                    this,
                    "Доктор создан",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_OK, Intent().putExtra("isNew", true))
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Exception of adding record",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_CANCELED)
            } finally {
                finish()
            }
        }
    }

}

