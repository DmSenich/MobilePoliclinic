package ru.pin120.policlinic.details

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseController
import ru.pin120.policlinic.updates.DiseaseUpdateActivity

class DiseaseDetailsActivity  : ComponentActivity(){
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseController: DiseaseController
    private var visitingId = -1L
    private var diseaseId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_disease)

        mDBHelper = DatabaseHelper(this)
        diseaseController = DiseaseController(mDBHelper)

        val idTV: TextView = findViewById(R.id.tvId)
        val typeTV :TextView = findViewById(R.id.type)
        val descriptionTV:TextView = findViewById(R.id.description)
        setDiseaseView(idTV, typeTV, descriptionTV)

        val btnUpdate: Button = findViewById(R.id.bUpdate)
        val btnDelete:Button = findViewById(R.id.bDelete)
        btnUpdate.setOnClickListener {
            val disease = diseaseController.getDiseaseById(intent.extras?.getLong("id")!!)
            val intent = Intent(this@DiseaseDetailsActivity, DiseaseUpdateActivity::class.java)
            intent.putExtra("id",  idTV.text.toString().toLong())
            intent.putExtra("visitingId",  disease?.visiting?.id)
            startActivityForResult(intent, 0)
        }
        btnDelete.setOnClickListener {
            try {
                diseaseController.deleteDisease(diseaseId)
                setResult(RESULT_OK, intent)
                finish()
            }catch (ex:Exception){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val idTV: TextView = findViewById(R.id.tvId)
        val typeTV :TextView = findViewById(R.id.type)
        val descriptionTV:TextView = findViewById(R.id.description)
        if(RESULT_OK == resultCode){
            intent.putExtra("id", data!!.getLongExtra("id", -1))
            setDiseaseView(idTV, typeTV, descriptionTV)
        }

    }

    private fun setDiseaseView(
        idTV: TextView,
        typeTV: TextView,
        descriptionTV: TextView
    ){
        if(intent.extras?.getLong("id") != -1L){
            diseaseId = intent.extras?.getLong("id")!!
            idTV.text = diseaseId.toString()
            val disease = diseaseController.getDiseaseById(diseaseId!!)
            visitingId = disease?.visiting?.id!!
            if(disease != null){
                typeTV.text = disease.diseaseType?.name
                descriptionTV.text = disease.description
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        intent.putExtra("id", visitingId)
        setResult(RESULT_CANCELED, intent)
        finish()
        return true
    }

}