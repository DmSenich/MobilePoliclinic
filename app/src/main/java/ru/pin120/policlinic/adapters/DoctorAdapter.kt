package ru.pin120.policlinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.DiseaseType
import ru.pin120.policlinic.models.Doctor

class DoctorAdapter(context: Context, private val resource: Int, objects: List<Doctor>) :
    ArrayAdapter<Doctor>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView ?:
        LayoutInflater.from(context).inflate(R.layout.adapter_item_doctors, parent, false)

        val doctor = getItem(position)

        val idTextView: TextView = itemView!!.findViewById(R.id.id)
        val tvFIO:TextView = itemView!!.findViewById(R.id.tvFIO)
//        val lastNameTextView: TextView = itemView.findViewById(R.id.lastName)
//        val firstNameTextView: TextView = itemView.findViewById(R.id.firstName)
//        val patrTextView: TextView = itemView.findViewById(R.id.patr)
//        val workExpTextView: TextView = itemView.findViewById(R.id.workExp)

        idTextView.text = doctor?.id.toString()
//        var stringBuilder:StringBuilder = java.lang.StringBuilder()
//        stringBuilder.append(doctor?.lastName.toString() + " " + doctor?.firstName.toString() + " " + doctor?.patr.toString())
        tvFIO.text = doctor?.lastName + " " + doctor?.firstName
        if(doctor?.patr != null){
            tvFIO.text = tvFIO.text.toString() + " " + doctor?.patr
        }
//        lastNameTextView.text = "Last Name: ${doctor?.lastName}"
//        firstNameTextView.text = "First Name: ${doctor?.firstName}"
//        patrTextView.text = "Patr: ${doctor?.patr}"
//        workExpTextView.text = "Work Exp: ${doctor?.workExp} years"

        return itemView
    }
}