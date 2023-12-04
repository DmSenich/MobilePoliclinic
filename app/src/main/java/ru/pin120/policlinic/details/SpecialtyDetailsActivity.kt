package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Specialty
import ru.pin120.policlinic.updates.SpecialtyUpdateActivity

class SpecialtyDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_specialty)
        val btnUpdate : Button = findViewById(R.id.bUpdate)
        mDBHelper = DatabaseHelper(this)
        specialtyController = SpecialtyController(mDBHelper)

        val tvId:TextView = findViewById(R.id.tvId)
        val tvName:TextView = findViewById(R.id.etName)
        setSpecialtyView(tvId, tvName)

        btnUpdate.setOnClickListener {
            val intent = Intent(this@SpecialtyDetailsActivity, SpecialtyUpdateActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tvId:TextView = findViewById(R.id.tvId)
        val tvName:TextView = findViewById(R.id.etName)
        if(resultCode == Activity.RESULT_OK){
//            mDBHelper = DatabaseHelper(this)
//            specialtyController = SpecialtyController(mDBHelper)
//            val id = intent.extras!!.getLong("id");
//            val specialty = specialtyController.getSpecialtyById(id)
            setSpecialtyView(tvId, tvName)
        }
    }
    private fun setSpecialtyView(tvId:TextView, tvName:TextView){
        if(intent.extras?.getLong("id") != null){
            tvId.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id");
            val specialty = specialtyController.getSpecialtyById(id)
            tvName.text = specialty!!.name
        }
    }
}