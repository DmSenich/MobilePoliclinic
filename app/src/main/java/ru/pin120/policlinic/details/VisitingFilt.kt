package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VisitingFilt : ComponentActivity(){

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController
    private lateinit var patientController: PatientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filt_visiting)

        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)
        doctorController = DoctorController(mDBHelper)

        val doctorIdTV: TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV: TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val minDateEt: DatePicker = findViewById(R.id.etDateMin)
        val maxDateEt:DatePicker = findViewById(R.id.etDateMax)
        val isMinDate:CheckBox = findViewById(R.id.isMinDate)
        val isMaxDate:CheckBox = findViewById(R.id.isMaxDate)

        val btnPatient: Button = findViewById(R.id.btnChoosePatient)
        val btnDoctor: Button = findViewById(R.id.btnChooseDoctor)
        val btnSave: Button = findViewById(R.id.OK)

        btnDoctor.setOnClickListener {
            intent = Intent(this@VisitingFilt, VisitingDoctorActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnPatient.setOnClickListener {
            intent = Intent(this@VisitingFilt, VisitingPatientActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnSave.setOnClickListener {
            val doctorId = doctorIdTV.text.toString().toLongOrNull()
            val patientId = patientIdTV.text.toString().toLongOrNull()
            val intent = Intent()
            if(doctorId != null && doctorId != 0L) intent.putExtra("doctorId", doctorId) else intent.putExtra("doctorId", 0L)
            if(patientId != null && patientId != 0L) intent.putExtra("patientId", patientId) else intent.putExtra("patientId", 0L)
            val minDate:String?
            val maxDate:String?
            if(isMinDate.isChecked){
                val year = minDateEt.year
                val month = minDateEt.month
                val day = minDateEt.dayOfMonth
                minDate = "$year-${month + 1}-$day"
            }
            else minDate = null
            if(isMaxDate.isChecked){
                val year = maxDateEt.year
                val month = maxDateEt.month
                val day = maxDateEt.dayOfMonth
                maxDate = "$year-${month + 1}-$day"
            }else maxDate = null
            intent.putExtra("minDate", minDate)
            intent.putExtra("maxDate", maxDate)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val doctorIdTV: TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV: TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        if (resultCode == Activity.RESULT_OK) {
            val doctorId = data!!.getLongExtra("doctorId", -1)
            if(doctorId != -1L){
                intent.putExtra("doctorId", doctorId)
            }
            val patientId = data!!.getLongExtra("patientId", -1)
            if(patientId != -1L){
                intent.putExtra("patientId", patientId)
            }
            setVisitingView(doctorIdTV, doctorTV, patientIdTV, patientTV)
        }
    }
    private fun setVisitingView(
        doctorIdTV: TextView,
        doctorTV: TextView,
        patientIdTV: TextView,
        patientTV: TextView,
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