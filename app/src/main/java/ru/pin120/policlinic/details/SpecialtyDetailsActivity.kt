package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Specialty
import ru.pin120.policlinic.updates.SpecialtyUpdateActivity

class SpecialtyDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController
    private var specialtyId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_specialty)
        val btnUpdate : Button = findViewById(R.id.bUpdate)
        val btnDelete :Button = findViewById(R.id.bDelete)

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
        btnDelete.setOnClickListener {
            try {
                specialtyController.deleteSpecialty(specialtyId)
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
            specialtyId = intent.extras?.getLong("id")!!
            tvId.text = specialtyId.toString()
            val specialty = specialtyController.getSpecialtyById(specialtyId)
            tvName.text = specialty!!.name
        }
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        setResult(RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
}