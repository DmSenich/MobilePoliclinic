package ru.pin120.policlinic

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.pin120.policlinic.ui.theme.PoliclinicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bDoctors:Button = findViewById(R.id.bDoctors)
        val bPatients:Button = findViewById(R.id.bPatients)
        val bSpecialties:Button = findViewById(R.id.bSpecialties)
        val bDiseaseTypes:Button = findViewById(R.id.bDiseaseTypes)
        val bVisitings:Button = findViewById(R.id.bVisitings)
        bDoctors.setOnClickListener {
            val intent = Intent(this@MainActivity, DoctorMainActivity::class.java)
            startActivity(intent)
        }
        bPatients.setOnClickListener {
            val intent = Intent(this@MainActivity, PatientMainActivity::class.java)
            startActivity(intent)
        }
        bSpecialties.setOnClickListener {
            val intent = Intent(this@MainActivity,SpecialtyMainActivity::class.java)
            startActivity(intent)
        }
        bDiseaseTypes.setOnClickListener {
            val intent = Intent(this@MainActivity, DiseaseTypeMainActivity::class.java)
            startActivity(intent)
        }
        bVisitings.setOnClickListener {
            val intent = Intent(this@MainActivity, VisitingMainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.doctors ->{
                val intent = Intent(this@MainActivity, DoctorMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.patients ->{
                val intent = Intent(this@MainActivity, PatientMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.specialties->{
                val intent = Intent(this@MainActivity,SpecialtyMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.disease_types ->{
                val intent = Intent(this@MainActivity, DiseaseTypeMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.visitings ->{
                val intent = Intent(this@MainActivity, VisitingMainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
