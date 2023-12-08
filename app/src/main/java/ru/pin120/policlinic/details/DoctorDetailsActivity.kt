package ru.pin120.policlinic.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.joinAll
import org.w3c.dom.Text
import ru.pin120.policlinic.DatabaseHelper
import ru.pin120.policlinic.R
import ru.pin120.policlinic.controllers.DoctorController
import ru.pin120.policlinic.updates.DoctorSpecialtiesActivity
import ru.pin120.policlinic.updates.DoctorUpdateActivity
import kotlin.text.StringBuilder

class DoctorDetailsActivity : ComponentActivity() {

    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var doctorController: DoctorController
    private var doctorId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_doctor)

        mDBHelper = DatabaseHelper(this)
        doctorController = DoctorController(mDBHelper)

        val tvId: TextView = findViewById(R.id.tvId)
        val tvLastName: TextView = findViewById(R.id.tvLastName)
        val tvFirstName: TextView = findViewById(R.id.tvFirstName)
        val tvPatr: TextView = findViewById(R.id.tvPatr)
        val tvWorkExp: TextView = findViewById(R.id.tvWorkExp)
        val tvSpecialties:TextView = findViewById(R.id.tvSpecialties)
        val btnUpdate: Button = findViewById(R.id.bUpdate)
        val btnUpSpecialties :Button = findViewById(R.id.bSpecialty)

        setDoctorView(tvId, tvLastName, tvFirstName, tvPatr, tvWorkExp, tvSpecialties)

        btnUpdate.setOnClickListener {
            val intent = Intent(this@DoctorDetailsActivity, DoctorUpdateActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
        btnUpSpecialties.setOnClickListener {
            val intent = Intent(this@DoctorDetailsActivity, DoctorSpecialtiesActivity::class.java)
            intent.putExtra("id", tvId.text.toString().toLong())
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tvId: TextView = findViewById(R.id.tvId)
        val tvLastName: TextView = findViewById(R.id.tvLastName)
        val tvFirstName: TextView = findViewById(R.id.tvFirstName)
        val tvPatr: TextView = findViewById(R.id.tvPatr)
        val tvWorkExp: TextView = findViewById(R.id.tvWorkExp)
        val tvSpecialties:TextView = findViewById(R.id.tvSpecialties)
        intent.putExtra("id", data!!.getLongExtra("id", -1))
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            setDoctorView(tvId, tvLastName, tvFirstName, tvPatr, tvWorkExp, tvSpecialties)
        }
    }

    private fun setDoctorView(
        tvId: TextView,
        tvLastName: TextView,
        tvFirstName: TextView,
        tvPatr: TextView,
        tvWorkExp: TextView,
        tvSpecialties:TextView
    ) {
        if (intent.extras?.getLong("id") != -1L) {
            doctorId = intent.extras?.getLong("id")!!
            tvId.text = doctorId.toString()
            val doctor = doctorController.getDoctorById(doctorId)
            if (doctor != null) {
                tvLastName.text = doctor.lastName
                tvFirstName.text = doctor.firstName
                tvPatr.text = doctor.patr ?: ""
                tvWorkExp.text = doctor.workExp.toString()

                val sBuilder = StringBuilder()
                doctor.specialties.forEach{sBuilder.append(it.name).append(",")}
                tvSpecialties.text =sBuilder.removeSuffix(",").toString()
            }
        }
    }
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return super.onMenuItemSelected(featureId, item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}