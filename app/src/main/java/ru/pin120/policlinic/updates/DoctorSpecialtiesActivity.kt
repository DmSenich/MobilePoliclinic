package ru.pin120.policlinic.updates

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.adapters.DoctorSpecialtyAdapter
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.SpecialtyController

class DoctorSpecialtiesActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController
    private lateinit var specialtyController: SpecialtyController
    private var doctorId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_doctor_specialties)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)
        specialtyController = SpecialtyController(mDBHelper)

        val tvDoctorId: TextView = findViewById(R.id.tvDoctorId)
        doctorId = intent.extras?.getLong("id")
        tvDoctorId.text = doctorId.toString()

        val specialties = specialtyController.getAllSpecialties()
        val doctorSpecialties = doctorController.getDoctorSpecialties(doctorId!!)

        val adapter = DoctorSpecialtyAdapter(this, R.layout.adapter_item_doctor_specialties, specialties, doctorSpecialties)
        val listView: ListView = findViewById(R.id.listView)
        val btnSave: Button = findViewById(R.id.OK)

        listView.adapter = adapter
        intent.putExtra("id", doctorId)

        btnSave.setOnClickListener {
            val selectedSpecialties = adapter.getSelectedSpecialties()
            doctorController.updateDoctorSpecialties(doctorId!!, selectedSpecialties)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        intent.putExtra("id", doctorId)
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }


}