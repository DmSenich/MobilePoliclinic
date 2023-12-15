package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.w3c.dom.Text
import ru.pin120.policlinic.adapters.DoctorAdapter
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.details.DoctorDetailsActivity
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.news.DoctorNewActivity
import ru.pin120.policlinic.updates.DoctorSpecialtiesActivity

class DoctorMainActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController
    private val specialtiesId = ArrayList<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)

        val doctors = doctorController.getAllDoctors()
        val adapter = DoctorAdapter(this, R.layout.adapter_item_doctors, doctors)

        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        val btnFilt:Button = findViewById(R.id.bFilt)
        btnFilt.visibility = View.VISIBLE
        val tFilt: TextView = findViewById(R.id.tFilt)
        tFilt.visibility = View.VISIBLE
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val idTV: TextView = view.findViewById(R.id.id)
            val tvFIO:TextView = view.findViewById(R.id.tvFIO)
//            val lastNameTV: TextView = view.findViewById(R.id.lastName)
//            val firstNameTV: TextView = view.findViewById(R.id.firstName)
//            val patrTV: TextView = view.findViewById(R.id.patr)
//            val workExpTV:TextView = view.findViewById(R.id.workExp)
            val intent = Intent(this@DoctorMainActivity, DoctorDetailsActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@DoctorMainActivity, DoctorNewActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnFilt.setOnClickListener {
            val intent = Intent(this@DoctorMainActivity, DoctorSpecialtiesActivity::class.java)
            intent.putExtra("isFilt", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val specLine = data?.extras?.getString("specialtiesId")
            if(specLine != null){
                specialtiesId.clear()
                if(specLine != "")
                    for(sp in specLine!!.split(",")){
                        specialtiesId.add(sp.toLong())
                    }
            }
            lateinit var doctors:List<Doctor>
            if(specialtiesId.isEmpty()){
                doctors = doctorController.getAllDoctors()
            }
            else{
                doctors = doctorController.getDoctorsBySpecialtiesId(specialtiesId)
            }
//            val doctors = doctorController.getAllDoctors()
            val adapter = DoctorAdapter(this, R.layout.adapter_item_doctors, doctors)

            val listView: ListView = findViewById(R.id.listView)
            listView.adapter = adapter
        }
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
}