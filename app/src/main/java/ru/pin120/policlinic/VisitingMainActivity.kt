package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.VisitingAdapter
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.details.VisitingDetailsActivity
import ru.pin120.policlinic.details.VisitingFilt
import ru.pin120.policlinic.news.VisitingNewActivity
import ru.pin120.policlinic.updates.DoctorSpecialtiesActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VisitingMainActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        visitingController = VisitingController(mDBHelper)

        val visitings = visitingController.getAllVisitings()
        val adapter = VisitingAdapter(this, R.layout.adapter_item_visitings, visitings)

        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        val btnFilt:Button = findViewById(R.id.bFilt)
        btnFilt.visibility = View.VISIBLE
        val tFilt: TextView = findViewById(R.id.tFilt)
        tFilt.visibility = View.VISIBLE

        listView.adapter = adapter

        listView.setOnItemClickListener { _, view, _, _ ->
            val idTV: TextView = view.findViewById(R.id.id)
            val doctorTV: TextView = view.findViewById(R.id.doctorFIO)
            val patientTV: TextView = view.findViewById(R.id.patientFIO)
            val dateTV: TextView = view.findViewById(R.id.date)
            val intent = Intent(this@VisitingMainActivity, VisitingDetailsActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@VisitingMainActivity, VisitingNewActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnFilt.setOnClickListener {
            val intent = Intent(this@VisitingMainActivity, VisitingFilt::class.java)
//            intent.putExtra("isFilt", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val doctorId = data?.extras!!.getLong("doctorId")
            val patientId = data?.extras!!.getLong("patientId")
            val date1 = data.extras!!.getString("minDate")
            val date2 = data.extras!!.getString("maxDate")
            val minDate:Date?
            val maxDate:Date?
            if(date1 != null){
                minDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date1)!!
            }else minDate = null

            if(date2 != null){
                maxDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date2)!!
            }else maxDate = null
            val visitings = visitingController.getVisitingsByDoctorIdAndPatientId(doctorId, patientId, minDate, maxDate)
            val adapter = VisitingAdapter(this, R.layout.adapter_item_visitings, visitings)

            val listView: ListView = findViewById(R.id.listView)
            listView.adapter = adapter
        }
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
}
