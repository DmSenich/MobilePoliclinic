package ru.pin120.policlinic.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Specialty

class SpecialtyNewActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_specialty)
        val btnOK : Button = findViewById(R.id.OK)
        mDBHelper = DatabaseHelper(this)
        specialtyController = SpecialtyController(mDBHelper)

        val etName:EditText = findViewById(R.id.etName)

        btnOK.setOnClickListener {
            val specialty = Specialty(null,null)
            specialty.name = etName.text.toString().trim()
            try{
                specialtyController.addSpecialty(specialty)
            }
            catch (ex:Exception){Toast.makeText(
                this,
                "Exception of adding record",
                Toast.LENGTH_SHORT
            ).show()
            }
//            val resintent = Intent()
//            resintent.putExtra("id", specialty.id)
            intent.putExtra("isNew", true)
            setResult(Activity.RESULT_OK, intent)
            Toast.makeText(
                this,
                "Запись создана\n Name: ${specialty.name}",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}