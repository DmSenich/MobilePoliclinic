package ru.pin120.policlinic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.Disease

class DiseaseAdapter(context: Context, resource: Int, objects: List<Disease>) :
    ArrayAdapter<Disease>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView ?:
        LayoutInflater.from(context).inflate(R.layout.adapter_item_diseases, parent, false)

        val disease = getItem(position)

        val idTV: TextView = itemView.findViewById(R.id.id)
        val diseaseTypeTV: TextView = itemView.findViewById(R.id.type)
        val descriptionTV: TextView = itemView.findViewById(R.id.description)

        idTV.text = disease?.id.toString()
        diseaseTypeTV.text = disease?.diseaseType?.name
        descriptionTV.text = disease?.description

        return itemView
    }
}
