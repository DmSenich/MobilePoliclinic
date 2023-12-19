package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.VisitingAdapter
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.details.VisitingDetailsActivity
import ru.pin120.policlinic.details.VisitingFilt
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.models.Visiting
import ru.pin120.policlinic.news.VisitingNewActivity
import ru.pin120.policlinic.updates.DoctorSpecialtiesActivity
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VisitingMainActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController
    private var doctorId:Long = 0L
    private var patientId:Long = 0L
    private var date1:String? = null
    private var date2:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        visitingController = VisitingController(mDBHelper)

        val visitings = visitingController.getAllVisitings()
        val adapter = VisitingAdapter(this, R.layout.adapter_item_visitings, visitings)

        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        val btnLoad:Button = findViewById(R.id.bLoad)
        btnLoad.visibility = View.VISIBLE
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
        btnLoad.setOnClickListener {
            val minDate:Date?
            val maxDate:Date?
            if(date1 != null){
                minDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date1)!!
            }else minDate = null

            if(date2 != null){
                maxDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date2)!!
            }else maxDate = null
            val visitingsL = visitingController.getVisitingsByDoctorIdAndPatientId(doctorId, patientId, minDate, maxDate)
            val file = generateFile(visitingsL)
        }
    }
    private fun generateFile(visitings:List<Visiting>) : File {
        val fileName = "Список_визитов.txt"
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val downloadsDirectory = "/storage/emulated/0/Download"
        val file = File(downloadsDirectory, fileName)
        try{
            FileWriter(file).use{ writer ->
                if(doctorId != 0L){
                    writer.append("Доктор: ${visitings.first().doctor}\n")
                }
                if(patientId != 0L){
                    writer.append("Патиент: ${visitings.first().patient}\n")
                }
                if(date1 != null){
                    writer.append("Минимальная дата: ${date1}\n")
                }
                if(date2 != null){
                    writer.append("Максимальная дата: ${date2}\n")
                }
                visitings.forEach{ visiting ->
                    writer.append("$visiting\n")
                }
            }
            Toast.makeText(this, "Файл создан", Toast.LENGTH_SHORT).show()
        }catch(ex:Exception){
            Toast.makeText(this, "Файл не удалось записать", Toast.LENGTH_SHORT).show()
        }
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if(data?.extras?.getLong("doctorId") != null ||
                data?.extras?.getLong("patientId") != null ||
                data?.extras?.getString("minDate") != null ||
                data?.extras?.getString("maxDate") != null){
                doctorId = data?.extras!!.getLong("doctorId")
                patientId = data?.extras!!.getLong("patientId")
                date1 = data.extras!!.getString("minDate")
                date2 = data.extras!!.getString("maxDate")
            }
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
