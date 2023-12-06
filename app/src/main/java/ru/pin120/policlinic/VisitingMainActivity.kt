package ru.pin120.policlinic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.pin120.policlinic.adapters.VisitingAdapter
import ru.pin120.policlinic.controllers.VisitingController
import ru.pin120.policlinic.details.VisitingDetailsActivity
import ru.pin120.policlinic.news.VisitingNewActivity

class VisitingMainActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var visitingController: VisitingController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        mDBHelper = DatabaseHelper(this)
        visitingController = VisitingController(mDBHelper)

        val visitings = visitingController.getAllVisitings()
        val adapter = VisitingAdapter(this, R.layout.adapter_item_visitings, visitings)

        val listView: ListView = findViewById(R.id.listView)
        val btnNew: Button = findViewById(R.id.bNew)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, view, _, _ ->
            val idTV: TextView = view.findViewById(R.id.id)
            val doctorTV: TextView = view.findViewById(R.id.doctorFIO)
            val patientTV: TextView = view.findViewById(R.id.patientFIO)
            val dateTV: TextView = view.findViewById(R.id.date)
            val intent = Intent(this@VisitingMainActivity, VisitingDetailsActivity::class.java)
            intent.putExtra("id", idTV.text.toString().toLong())
            startActivity(intent)
        }

        btnNew.setOnClickListener {
            val intent = Intent(this@VisitingMainActivity, VisitingNewActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val visitings = visitingController.getAllVisitings()
            val adapter = VisitingAdapter(this, R.layout.adapter_item_visitings, visitings)

            val listView: ListView = findViewById(R.id.listView)
            listView.adapter = adapter
        }
    }

    override fun onDestroy() {
        mDBHelper.close()
        super.onDestroy()
    }
}
