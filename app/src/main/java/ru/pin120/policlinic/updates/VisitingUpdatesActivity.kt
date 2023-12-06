package ru.pin120.policlinic.updates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.w3c.dom.Text
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.models.Visiting
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingUpdatesActivity : ComponentActivity() {

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

        val idTV:TextView = findViewById(R.id.tvId)
        val doctorIdTV: TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV: TextView = findViewById(R.id.tvPatientId)
        val patientTV:TextView = findViewById(R.id.patientFIO)
        val doctorTV:TextView = findViewById(R.id.doctorFIO)

        val etDate: DatePicker = findViewById(R.id.etDate)

        val btnPatient: Button = findViewById(R.id.btnChoosePatient)
        val btnDoctor: Button = findViewById(R.id.btnChooseDoctor)
        val btnSave: Button = findViewById(R.id.OK)

        if (intent.extras?.getLong("id") != null) {
            val id = intent.extras!!.getLong("id")
            val visiting = visitingController.getVisitingById(id)
            idTV.text = id.toString()
            if(intent.extras?.getLong("doctorId") != 0L){
                val doctorId = intent.extras?.getLong("doctorId")
                val doctor = doctorController.getDoctorById(doctorId!!)
                doctorIdTV.text = doctorId.toString()
                doctorTV.text ="${doctor?.lastName + doctor?.firstName + doctor?.patr}"
            }else{
                doctorIdTV.text = visiting?.doctor?.id.toString()
                doctorTV.text = "${visiting?.doctor?.lastName + visiting?.doctor?.firstName + visiting?.doctor?.patr}"
            }
            if(intent.extras?.getLong("patientId")!= 0L){
                val patientId = intent.extras?.getLong("patientId")
                val patient = patientController.getPatientById(patientId!!)
                patientIdTV.text = patientId.toString()
                patientTV.text = "${patient?.lastName + patient?.firstName + patient?.patr}"
            }else{
                patientIdTV.text = visiting?.patient?.id.toString()
                patientTV.text = "${visiting?.patient?.lastName + visiting?.patient?.firstName + visiting?.patient?.patr}"
            }
            val year = visiting!!.date!!.year + 1900
            val month = visiting.date!!.month
            val day = visiting.date!!.date
            etDate.updateDate(year,month,day)
        }
        btnDoctor.setOnClickListener {
            intent = Intent(this@VisitingUpdatesActivity, VisitingDoctorActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnPatient.setOnClickListener {
            intent = Intent(this@VisitingUpdatesActivity, VisitingPatientActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }

        btnSave.setOnClickListener {
            val doctor = doctorController.getDoctorById(doctorIdTV.text.toString().toLong())
            val patient = patientController.getPatientById(patientIdTV.text.toString().toLong())
            val year = etDate.year
            val month = etDate.month
            val day = etDate.dayOfMonth
            val selectedDate = "$year-${month + 1}-$day"
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate)
            val visiting = Visiting(idTV.text.toString().toLong(), doctor, patient, date, ArrayList())
            try {
                visitingController.updateVisiting(visiting)
                Toast.makeText(
                    this,
                    "Запись обновлена",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Ошибка при обновлении записи",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                setResult(Activity.RESULT_OK, intent)
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
