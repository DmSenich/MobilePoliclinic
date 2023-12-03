package ru.pin120.policlinic

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.doctors ->{
                return true
            }
            R.id.patients ->{
                return true
            }
            R.id.specialties->{
                val intent = Intent(this@MainActivity,SpecialtyMainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.disease_types ->{
                return true
            }
            R.id.visitings ->{
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
