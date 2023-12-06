package ru.pin120.policlinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.Patient
import ru.pin120.policlinic.models.Visiting
import java.text.SimpleDateFormat
import java.util.Locale

class VisitingAdapter(context: Context, resource: Int, objects: List<Visiting>) :
    ArrayAdapter<Visiting>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView ?:
        LayoutInflater.from(context).inflate(R.layout.adapter_item_visitings, parent, false)

        val visiting = getItem(position)

        val idTV: TextView = itemView.findViewById(R.id.id)
//        val doctorIdTV:TextView = itemView.findViewById(R.id.tvDoctorId)
//        val patientIdTV:TextView = itemView.findViewById(R.id.tvPatientId)
        val doctorTV: TextView = itemView.findViewById(R.id.doctorFIO)
        val patientTV: TextView = itemView.findViewById(R.id.patientFIO)
        val dateTV: TextView = itemView.findViewById(R.id.date)

        idTV.text = visiting?.id.toString()
        doctorTV.text = "${visiting?.doctor?.lastName + visiting?.doctor?.firstName + visiting?.doctor?.patr}"
        patientTV.text = "${visiting?.patient?.lastName + visiting?.patient?.firstName + visiting?.patient?.patr}"
        dateTV.text = "${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(visiting?.date)}"

        return itemView
    }
}

