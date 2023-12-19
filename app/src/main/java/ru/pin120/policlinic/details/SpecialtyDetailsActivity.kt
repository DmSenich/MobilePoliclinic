package ru.pin120.policlinic.details

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.controllers.SpecialtyController
import ru.pin120.policlinic.models.Doctor
import ru.pin120.policlinic.models.Specialty
import ru.pin120.policlinic.updates.SpecialtyUpdateActivity
import java.io.File
import java.io.FileWriter

class SpecialtyDetailsActivity : ComponentActivity() {
    private lateinit var mDBHelper:DatabaseHelper
    private lateinit var specialtyController: SpecialtyController
    private var specialtyId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_specialty)
        val btnUpdate : Button = findViewById(R.id.bUpdate)
        val btnDelete :Button = findViewById(R.id.bDelete)
        val btnLoad:Button = findViewById(R.id.bLoad)
        mDBHelper = DatabaseHelper(this)
        specialtyController = SpecialtyController(mDBHelper)
        val doctorController = DoctorController(mDBHelper)
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
        btnLoad.setOnClickListener {
            val specialtyName = tvName.text.toString()
            val doctors = doctorController.getDoctorsBySpecialtiesId(arrayListOf(specialtyId))

            val file = generateFile(doctors, specialtyName)

        }
    }

    private fun generateFile(doctors:List<Doctor>, specialtyName:String) :File {
        val fileName = "Список_докторов_${specialtyName}.txt"
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val downloadsDirectory = "/storage/emulated/0/Download"
        val file = File(downloadsDirectory, fileName)
        try{
            FileWriter(file).use{writer ->
                writer.append("$specialtyName\n")
                doctors.forEach{ doctor ->
                    writer.append("$doctor\n")
                }
            }
            Toast.makeText(this, "Файл создан", Toast.LENGTH_SHORT).show()
        }catch(ex:Exception){
            Toast.makeText(this, "Файл не удалось записать", Toast.LENGTH_SHORT).show()
        }
        return file
    }
//    private fun dowloadFile(file:File){
//        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
//        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val request = DownloadManager.Request(uri)
//
//        request.setTitle("Downloading Doctors List")
//        request.setDescription("File is being downloaded...")
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.name)
//        val downloadId = downloadManager.enqueue(request)
//        Toast.makeText(this, "Downloading Doctors List...", Toast.LENGTH_SHORT).show()
//    }
    private fun setSpecialtyView(tvId:TextView, tvName:TextView){
        if(intent.extras?.getLong("id") != null){
            specialtyId = intent.extras?.getLong("id")!!
            tvId.text = specialtyId.toString()
            val specialty = specialtyController.getSpecialtyById(specialtyId)
            tvName.text = specialty!!.name
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