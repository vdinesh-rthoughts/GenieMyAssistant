package com.rthoughts.readallsms

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.rthoughts.readallsms.databinding.SmsRecordLayoutBinding

class SmsListAdapter(val context: Context, private val smsList: ArrayList<SmsData>) :
    RecyclerView.Adapter<SmsListAdapter.MyViewHolder>(), ListAdapter {

    class MyViewHolder(val binding: SmsRecordLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SmsRecordLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.smsSender.text = smsList[position].senderName
        holder.binding.smsMessage.text = smsList[position].message
        holder.binding.smsDate.text = smsList[position].date
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        observer.toString()
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        observer.toString()
    }

    override fun getCount(): Int {
        return smsList.size
    }

    override fun getItem(position: Int): Any {
        return smsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.sms_record_layout, parent, false)
        }
        val currentItem = getItem(position) as SmsData
        val textViewItemName: AppCompatTextView = convertView
            ?.findViewById(R.id.sms_sender)!!
        val textViewItemDescription: AppCompatTextView = convertView
            .findViewById(R.id.sms_message)!!
        val date: AppCompatTextView = convertView
            .findViewById(R.id.sms_date)!!
        textViewItemName.text = currentItem.senderName
        textViewItemDescription.text = currentItem.message
        date.text = currentItem.date
        return convertView
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return smsList.isEmpty()
    }

    override fun areAllItemsEnabled(): Boolean {
        return !isEmpty
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    /*fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //val view = LayoutInflater.from(context).inflate(R.layout.sms_record_layout, parent, false)
        //binding=SmsRecordLayoutBinding.inflate(layoutInflater)


        view.sms_sender.text = smsList[position].senderName
        view.sms_date.text = smsList[position].message
        view.sms_message.text = smsList[position].date
        return view
    }*/


}