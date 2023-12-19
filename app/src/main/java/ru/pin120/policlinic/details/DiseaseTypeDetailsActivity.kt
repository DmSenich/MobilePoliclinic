package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseController
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.models.Disease
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.updates.DiseaseTypeUpdateActivity
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.typeOf

class DiseaseTypeDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseTypeController: DiseaseTypeController
    private var diseaseTypeId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_disease_type)
        val btnUpdate: Button = findViewById(R.id.bUpdate)
        val btnDelete:Button = findViewById(R.id.bDelete)
        val btnLoad:Button = findViewById(R.id.bLoad)
        mDBHelper = DatabaseHelper(this)
        diseaseTypeController = DiseaseTypeController(mDBHelper)
        val diseaseController = DiseaseController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val tvName: TextView = findViewById(R.id.etName)
        setDiseaseTypeView(tvId, tvName)

        btnUpdate.setOnClickListener {
            val intent = Intent(this@DiseaseTypeDetailsActivity, DiseaseTypeUpdateActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnDelete.setOnClickListener {
            try {
                diseaseTypeController.deleteDiseaseType(diseaseTypeId)
                val intent =Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            catch (ex:Exception){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnLoad.setOnClickListener {
            val diseaseName = tvName.text.toString()
            val diseases = diseaseController.getDiseasesByTypesId(arrayListOf(diseaseTypeId))

            val file = generateFile(diseases, diseaseName)
        }
    }

    private fun generateFile(diseases:List<Disease>, diseaseName:String) : File {
        val timeNow = SimpleDateFormat("yyyy-MM-dd--hh-mm-ss", Locale.getDefault()).format(Date())
        val fileName = "Список_заболеваний_${diseaseName}_${timeNow}.txt"
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val downloadsDirectory = "/storage/emulated/0/Download"
        val file = File(downloadsDirectory, fileName)
        try{
            FileWriter(file).use{ writer ->
                var count = 0
                writer.append("Название диагноза: $diseaseName\n\n")
                writer.append("Общее количество поставленных диагнозов: ${diseases.count()}\n")
                diseases.forEach{ disease ->
                    count++
                    writer.append("${count}) ")
                    val date = disease.visiting?.date
                    writer.append("Дата: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)}\n")
                    val patient = disease.visiting?.patient
                    writer.append("Пациент: ${patient?.lastName} ${patient?.firstName}")
                    if(patient?.patr != null){
                        writer.append(" ${patient?.patr}")
                    }
                    writer.append("\n")
                    writer.append("Описание: ${disease.description}\n\n")
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
        val tvId: TextView = findViewById(R.id.tvId)
        val tvName: TextView = findViewById(R.id.etName)
        if (resultCode == Activity.RESULT_OK) {
            setDiseaseTypeView(tvId, tvName)
        }
    }

    private fun setDiseaseTypeView(tvId: TextView, tvName: TextView) {
        if (intent.extras?.getLong("id") != null) {
            diseaseTypeId = intent.extras?.getLong("id")!!
            tvId.text = diseaseTypeId.toString()
            val diseaseType = diseaseTypeController.getDiseaseTypeById(diseaseTypeId)
            tvName.text = diseaseType!!.name
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
