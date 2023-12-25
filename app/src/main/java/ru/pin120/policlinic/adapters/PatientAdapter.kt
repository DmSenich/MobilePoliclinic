package ru.pin120.policlinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.Patient

class PatientAdapter(context: Context, resource: Int, objects: List<Patient>) :
    ArrayAdapter<Patient>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView ?:
        LayoutInflater.from(context).inflate(R.layout.adapter_item_patients, parent, false)

        val patient = getItem(position)

        val idTV: TextView = itemView!!.findViewById(R.id.id)
        val tvFIO:TextView = itemView!!.findViewById(R.id.tvFIO)
//        val lastNameTV: TextView = convertView.findViewById(R.id.lastName)
//        val firstNameTV: TextView = convertView.findViewById(R.id.firstName)
//        val patrTV: TextView = convertView.findViewById(R.id.patr)
//        val dateBirthTV: TextView = convertView.findViewById(R.id.dateBirth)
//        val areaTV: TextView = convertView.findViewById(R.id.area)
//        val cityTV: TextView = convertView.findViewById(R.id.city)
//        val houseTV: TextView = convertView.findViewById(R.id.house)
//        val apartmentTV: TextView = convertView.findViewById(R.id.apartment)

        idTV.text = patient?.id.toString()
        tvFIO.text = patient?.lastName + " " + patient?.firstName
        if(patient?.patr != null){
            tvFIO.text = tvFIO.text.toString() + " " + patient?.patr
        }
//        lastNameTV.text = patient?.lastName
//        firstNameTV.text = patient?.firstName
//        patrTV.text = patient?.patr ?: ""
//        val year = patient?.dateBirth?.year!! + 1900
//        val month = patient?.dateBirth?.month
//        val day = patient?.dateBirth?.date
//        val selectedDate = "$year-${month?.plus(1)}-$day"
//        dateBirthTV.text = selectedDate.toString()
//        areaTV.text = patient?.area
//        cityTV.text = patient?.city
//        houseTV.text = patient?.house
//        if(patient?.apartment == 0L){
//            apartmentTV.text == ""
//        }
//        else{
//            apartmentTV.text = patient?.apartment.toString()
//        }


        return itemView
    }
}
