package ru.pin120.policlinic.details

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.updates.VisitingUpdatesActivity
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingDetailsActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController
//    private lateinit var visiting: Visiting

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
        if (intent.extras?.getLong("id") != null) {
            val id = intent.extras!!.getLong("id")

        }

        btnUpdate.setOnClickListener {
            val intent = Intent(this@VisitingDetailsActivity, VisitingUpdatesActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnDisease.setOnClickListener {
            val intent = Intent(this@VisitingDetailsActivity, VisitingDiseasesActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val idTV: TextView = findViewById(R.id.id)
        val doctorIdTV:TextView = findViewById(R.id.tvDoctorId)
        val patientIdTV:TextView = findViewById(R.id.tvPatientId)
        val doctorTV: TextView = findViewById(R.id.doctorFIO)
        val patientTV: TextView = findViewById(R.id.patientFIO)
        val dateTV: TextView = findViewById(R.id.tvDate)

        setVisitingView(idTV, doctorTV, patientTV, dateTV)
    }

    private fun setVisitingView(
        idTV: TextView,
        doctorTV: TextView,
        patientTV: TextView,
        dateTV: TextView
    ) {
        if (intent.extras?.getLong("id") != null) {
            idTV.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id")
            val visiting = visitingController.getVisitingById(id!!)
            if (visiting != null) {
                doctorTV.text = visiting.doctor!!.lastName + " " + visiting.doctor!!.firstName + " " + visiting.doctor!!.patr
                patientTV.text = visiting.patient!!.lastName + " " + visiting.patient!!.firstName + " " + visiting.patient!!.patr
                dateTV.text =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(visiting.date)
            }
        }
    }
}