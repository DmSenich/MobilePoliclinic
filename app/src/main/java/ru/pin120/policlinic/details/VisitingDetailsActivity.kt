package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.DiseaseMainActivity
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.updates.VisitingUpdateActivity
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingDetailsActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController
//    private lateinit var visiting: Visiting
    private var visitingId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_visiting)

        mDBHelper = DatabaseHelper(this)
        visitingController = VisitingController(mDBHelper)

        val idTV: TextView = findViewById(R.id.tvId)
//        val doctorIdTV:TextView = findViewById(R.id.tvDoctorId)
//        val patientIdTV:TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val dateTV: TextView = findViewById(R.id.tvDate)

        setVisitingView(idTV, doctorTV, patientTV, dateTV)

        val btnUpdate: Button = findViewById(R.id.bUpdate)
        val btnDisease:Button = findViewById(R.id.bDiseases)
        val btnDelete:Button = findViewById(R.id.bDelete)
//        if (intent.extras?.getLong("id") != null) {
//            val id = intent.extras!!.getLong("id")
//
//        }

        btnUpdate.setOnClickListener {
            val intent = Intent(this@VisitingDetailsActivity, VisitingUpdateActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnDisease.setOnClickListener {
            val intent = Intent(this@VisitingDetailsActivity, DiseaseMainActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnDelete.setOnClickListener {
            try {
                visitingController.deleteVisiting(visitingId)
                val intent =Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            catch (ex:Exception){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val idTV: TextView = findViewById(R.id.tvId)
//        val doctorIdTV:TextView = findViewById(R.id.tvDoctorId)
//        val patientIdTV:TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val dateTV: TextView = findViewById(R.id.tvDate)
        if (resultCode == Activity.RESULT_OK){
            intent.putExtra("id", data!!.getLongExtra("id", -1))
            setVisitingView(idTV, doctorTV, patientTV, dateTV)
        }

    }

    private fun setVisitingView(
        idTV: TextView,
        doctorTV: TextView,
        patientTV: TextView,
        dateTV: TextView
    ) {
        if (intent.extras?.getLong("id") != -1L) {
            visitingId = intent.extras!!.getLong("id")
            idTV.text = visitingId.toString()
            val visiting = visitingController.getVisitingById(visitingId!!)
            if (visiting != null) {
                doctorTV.text = visiting.doctor!!.lastName + " " + visiting.doctor!!.firstName + " " + visiting.doctor!!.patr
                patientTV.text = visiting.patient!!.lastName + " " + visiting.patient!!.firstName + " " + visiting.patient!!.patr
                dateTV.text =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(visiting.date)
            }
        }
    }
}