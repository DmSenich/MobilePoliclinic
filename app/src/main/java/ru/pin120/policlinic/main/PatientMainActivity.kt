package ru.pin120.policlinic.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.PatientAdapter
import ru.pin120.policlinic.controllers.PatientController
import ru.pin120.policlinic.details.PatientDetailsActivity
import ru.pin120.policlinic.news.PatientNewActivity

class PatientMainActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var patientController: PatientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        patientController = PatientController(mDBHelper)
        val patients = patientController.getAllPatients()

        val adapter = PatientAdapter(this, R.layout.adapter_item_patients, patients)
        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val idTV: TextView = view.findViewById(R.id.id)
            val tvFIO:TextView = view.findViewById(R.id.tvFIO)
//            val lastNameTV: TextView = view.findViewById(R.id.lastName)
//            val firstNameTV: TextView = view.findViewById(R.id.firstName)
//            val patrTV: TextView = view.findViewById(R.id.patr)
//            val dateBirthTV: TextView = view.findViewById(R.id.dateBirth)
//            val areaTV: TextView = view.findViewById(R.id.area)
//            val cityTV: TextView = view.findViewById(R.id.city)
//            val houseTV: TextView = view.findViewById(R.id.house)
//            val apartmentTV: TextView = view.findViewById(R.id.apartment)

            val selectedId = idTV.text.toString().toLong()
//            val selectedLastName = lastNameTV.text.toString()
//            val selectedFirstName = firstNameTV.text.toString()
//            val selectedPatr = patrTV.text.toString()
//            val selectedDateBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                .parse(dateBirthTV.text.toString())!!
//            val selectedArea = areaTV.text.toString()
//            val selectedCity = cityTV.text.toString()
//            val selectedHouse = houseTV.text.toString()
//            val selectedApartment = apartmentTV.text.toString()

            val intent = Intent(this@PatientMainActivity, PatientDetailsActivity::class.java)
            intent.putExtra("id", selectedId)
            startActivityForResult(intent, 0)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@PatientMainActivity, PatientNewActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val patients = patientController.getAllPatients()

            val adapter = PatientAdapter(this, R.layout.adapter_item_patients, patients)
            val listView: ListView = findViewById(R.id.listView)
            listView.adapter = adapter
        }
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
}
