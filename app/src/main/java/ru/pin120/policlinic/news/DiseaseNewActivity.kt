package ru.pin120.policlinic.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.w3c.dom.Text
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DiseaseController
import ru.pin120.policlinic.controllers.DiseaseTypeController
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.models.Disease
import ru.pin120.policlinic.updates.DiseaseDiseaseTypeActivity

class DiseaseNewActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseController: DiseaseController
    private lateinit var diseaseTypeController: DiseaseTypeController
    private lateinit var visitingController: VisitingController

    private var visitingId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_disease)

        mDBHelper = DatabaseHelper(this)
        diseaseController = DiseaseController(mDBHelper)
        diseaseTypeController = DiseaseTypeController(mDBHelper)
        visitingController = VisitingController(mDBHelper)

        val etDescription: EditText = findViewById(R.id.etDescription)
        val tvVisitingId:TextView = findViewById(R.id.tvVisitingId)
        val tvTypeId:TextView = findViewById(R.id.tvTypeId)
        val tvType:TextView = findViewById(R.id.type)


        val btnType:Button = findViewById(R.id.btnChooseType)
        val btnSave: Button = findViewById(R.id.OK)

        visitingId = intent.getLongExtra("visitingId", -1)

        if (visitingId == -1L) {
            Toast.makeText(this, "Ошибка: visitingId не указан", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        btnType.setOnClickListener {
            intent = Intent(this@DiseaseNewActivity, DiseaseDiseaseTypeActivity::class.java)
            startActivityForResult(intent, 0)
        }
        btnSave.setOnClickListener {
            val disease = Disease(null,null,null,null)
            val description = etDescription.text.toString().trim()
            var diseaseTypeId = 0L
            if(tvTypeId.text.toString() == ""){
                diseaseTypeId = 0L
            }
            else{
                diseaseTypeId = tvTypeId.text.toString().toLong()
            }
            val diseaseType = diseaseTypeController.getDiseaseTypeById(diseaseTypeId)
            val visiting = visitingController.getVisitingById(visitingId)

            if (diseaseType != null) {
                disease.description = description
                disease.diseaseType = diseaseType
                if(description == null || description == ""){
                    Toast.makeText(this, "Введите описание", Toast.LENGTH_LONG).show()
                }
                else{
                    try {
                        if (visiting != null) {
                            disease.visiting = visiting
                            diseaseController.addDisease(disease)
                            Toast.makeText(this, "Болезнь добавлена", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                        } else {
                            Toast.makeText(this, "Ошибка: посещение не найдено", Toast.LENGTH_LONG).show()
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(this, "Ошибка при добавлении записи", Toast.LENGTH_LONG).show()
                    }
                    finally {
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Выберите тип болезни", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val etDescription: EditText = findViewById(R.id.etDescription)
        val tvVisitingId:TextView = findViewById(R.id.tvVisitingId)
        val tvTypeId:TextView = findViewById(R.id.tvTypeId)
        val tvType:TextView = findViewById(R.id.type)
        if(resultCode == Activity.RESULT_OK){
            val diseaseTypeId = data!!.getLongExtra("typeId", -1)
            if(diseaseTypeId != -1L){
                intent.putExtra("typeId", diseaseTypeId)
            }
            setDiseaseView(tvVisitingId, tvTypeId, tvType, etDescription)
        }

    }
    private fun setDiseaseView(
        tvVisitingId:TextView,
        tvTypeId:TextView,
        tvType:TextView,
        etDescription:EditText
    ){
        if(intent.extras?.getLong("typeId") != 0L){
            val typeId = intent.extras?.getLong("typeId")
            val type = diseaseTypeController.getDiseaseTypeById(typeId!!)
            tvTypeId.text = typeId.toString()
            tvType.text = type?.name

        }
    }
    private fun back(){
        val intent = Intent()
        intent.putExtra("id", visitingId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        back()
        return true
    }

    override fun onBackPressed() {
        back()
        super.onBackPressed()
    }
}
