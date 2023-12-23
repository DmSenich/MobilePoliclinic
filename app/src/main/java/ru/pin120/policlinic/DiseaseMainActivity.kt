package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.DiseaseAdapter
import ru.pin120.policlinic.controllers.DiseaseController
import ru.pin120.policlinic.details.DiseaseDetailsActivity
import ru.pin120.policlinic.news.DiseaseNewActivity

class DiseaseMainActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var diseaseController: DiseaseController

    private var visitingId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        diseaseController = DiseaseController(mDBHelper)

        visitingId = intent.getLongExtra("id", -1)

        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)
        val diseases = diseaseController.getAllDiseasesForVisiting(visitingId)
        val adapter = DiseaseAdapter(this, R.layout.adapter_item_diseases, diseases)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val idTV:TextView = view.findViewById(R.id.id)
//            val typeTV:TextView = view.findViewById(R.id.type)
//            val descriptionTV:TextView = view.findViewById(R.id.description)
            val intent = Intent(this@DiseaseMainActivity, DiseaseDetailsActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivityForResult(intent, 0)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@DiseaseMainActivity, DiseaseNewActivity::class.java)
            intent.putExtra("visitingId", visitingId)
            startActivityForResult(intent, 0)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val diseases = diseaseController.getAllDiseasesForVisiting(visitingId)
            val adapter = DiseaseAdapter(this,R.layout.adapter_item_diseases, diseases)
            val listview:ListView = findViewById(R.id.listView)
            listview.adapter = adapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        intent.putExtra("id", visitingId)
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }

    private fun back(){
//        val intent = Intent()
//        intent.putExtra("id", visitingId)
//        setResult(Activity.RESULT_OK, intent)
//        finish()
    }
}
