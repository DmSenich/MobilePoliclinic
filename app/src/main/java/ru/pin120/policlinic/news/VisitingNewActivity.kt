package ru.pin120.policlinic.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.models.Visiting
import ru.pin120.policlinic.updates.VisitingDoctorActivity
import ru.pin120.policlinic.updates.VisitingPatientActivity
import ru.pin120.policlinic.updates.VisitingUpdatesActivity
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingNewActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController
    private lateinit var doctorController:DoctorController
    private lateinit var patientController: PatientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_visiting)

        mDBHelper = DatabaseHelper(this)
        visitingController = VisitingController(mDBHelper)
        patientController = PatientController(mDBHelper)
        doctorController = DoctorController(mDBHelper)

        val doctorIdTV: TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV: TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val dateTV: DatePicker = findViewById(R.id.etDate)

        val btnPatient: Button = findViewById(R.id.btnChoosePatient)
        val btnDoctor: Button = findViewById(R.id.btnChooseDoctor)
        val btnSave: Button = findViewById(R.id.OK)

        btnDoctor.setOnClickListener {
            intent = Intent(this@VisitingNewActivity, VisitingDoctorActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnPatient.setOnClickListener {
            intent = Intent(this@VisitingNewActivity, VisitingPatientActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnSave.setOnClickListener {
            val visiting = Visiting(null,null,null,null, ArrayList())
            val doctorId = doctorIdTV.text.toString().toLongOrNull()
            val patientId = patientIdTV.text.toString().toLongOrNull()
            val doctor = doctorController.getDoctorById(doctorId!!)
            val patient = patientController.getPatientById(patientId!!)
            visiting.doctor = doctor
            visiting.patient = patient

            val year = dateTV.year
            val month = dateTV.month
            val day = dateTV.dayOfMonth
            val selectedDate = "$year-${month + 1}-$day"
            visiting.date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(selectedDate)!!

            try {
                val id = visitingController.addVisiting(visiting)
                Toast.makeText(
                    this,
                    "Посещение создано",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_OK, Intent().putExtra("isNew", true))
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Ошибка при добавлении записи",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_CANCELED)
            } finally {
                finish()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val doctorIdTV: TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV: TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val dateTV: DatePicker = findViewById(R.id.etDate)
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            val doctorId = data!!.getLongExtra("doctorId", -1)
            if(doctorId != -1L){
                intent.putExtra("doctorId", doctorId)
            }
            val patientId = data!!.getLongExtra("patientId", -1)
            if(patientId != -1L){
                intent.putExtra("patientId", patientId)
            }
            setVisitingView(doctorIdTV, doctorTV, patientIdTV, patientTV, dateTV)
        }
    }
    private fun setVisitingView(
        doctorIdTV: TextView,
        doctorTV: TextView,
        patientIdTV: TextView,
        patientTV: TextView,
        dateTV: DatePicker
    ) {
//        dateTV.text =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(visiting.date)
        if (intent.extras?.getLong("doctorId") != 0L) {
            doctorIdTV.text = intent.extras?.getLong("doctorId").toString()
            val doctorId = intent.extras!!.getLong("doctorId")
            val doctor = doctorController.getDoctorById(doctorId!!)
            if (doctor != null) {
                doctorTV.text = doctor!!.lastName + " " + doctor!!.firstName + " " + doctor!!.patr
            }
        }
        if (intent.extras?.getLong("patientId") != 0L) {
            patientIdTV.text = intent.extras?.getLong("patientId").toString()
            val patientId = intent.extras!!.getLong("patientId")
            val patient = patientController.getPatientById(patientId!!)
            if (patient != null) {
                patientTV.text = patient!!.lastName + " " + patient!!.firstName + " " + patient!!.patr
            }
        }
    }
}
