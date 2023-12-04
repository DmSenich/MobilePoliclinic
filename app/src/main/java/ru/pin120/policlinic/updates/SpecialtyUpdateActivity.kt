package ru.pin120.policlinic.updates

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

class SpecialtyUpdateActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_specialty)
        val btnOK : Button = findViewById(R.id.OK)
        mDBHelper = DatabaseHelper(this)
        specialtyController = SpecialtyController(mDBHelper)

        val tvId:TextView = findViewById(R.id.tvId)
        val etName:EditText = findViewById(R.id.etName)

        if(intent.extras?.getLong("id") != null){
            tvId.text = intent.extras?.getLong("id").toString()
            val id = intent.extras!!.getLong("id");
            val specialty = specialtyController.getSpecialtyById(id)
            etName.setText(specialty!!.name)
        }
        btnOK.setOnClickListener {
            val specialty = Specialty(null,null)
            specialty.id = tvId.text.toString().toLong()
            specialty.name = etName.text.toString().trim()
            try{
                specialtyController.updateSpecialty(specialty)
                Toast.makeText(
                    this,
                    "Запись обновлена\n Name: ${specialty.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }catch (ex:Exception){
                Toast.makeText(
                    this,
                    "Exception of updating record",
                    Toast.LENGTH_SHORT
                ).show()
            }finally {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
//            val resintent = Intent()
//            resintent.putExtra("id", specialty.id)


        }
    }
}