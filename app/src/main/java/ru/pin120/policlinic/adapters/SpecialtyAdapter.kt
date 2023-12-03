package ru.pin120.policlinic.adapters

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.Specialty

class SpecialtyAdapter(context: Context, resource: Int, objects: List<Specialty>) : ArrayAdapter<Specialty>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.adapter_item_specialties, parent, false)
        val item = getItem(position)
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val idTextView: TextView = itemView.findViewById(R.id.id)
        nameTextView.text = item?.name
        idTextView.text = item?.id.toString()
        return itemView
    }
}