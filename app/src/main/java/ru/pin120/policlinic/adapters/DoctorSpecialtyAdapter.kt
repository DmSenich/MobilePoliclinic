package ru.pin120.policlinic.adapters

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import ru.pin120.policlinic.R
import ru.pin120.policlinic.models.Specialty

class DoctorSpecialtyAdapter(context: Context, resource: Int, specialties: List<Specialty>, val doctorSpecialties: List<Specialty>) : ArrayAdapter<Specialty>(context, resource, specialties) {
    private val checkedSpecialties = ArrayList<Specialty>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.adapter_item_doctor_specialties, parent, false)
        val item = getItem(position)

        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val idTextView: TextView = itemView.findViewById(R.id.id)
        val checkBox: CheckBox = itemView.findViewById(R.id.check)
        var b = doctorSpecialties.contains(item!!)
        checkBox.isChecked = b
        if(b){
            checkedSpecialties.add(item)
        }
        nameTextView.text = item?.name
        idTextView.text = item?.id.toString()
        checkBox.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                checkedSpecialties.add(item!!)
            }else{
                checkedSpecialties.remove(item!!)
            }
        }

        return itemView
    }
    fun getSelectedSpecialties() : ArrayList<Specialty>{
        return checkedSpecialties
    }


}