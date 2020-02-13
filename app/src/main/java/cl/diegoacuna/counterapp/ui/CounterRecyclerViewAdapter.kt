package cl.diegoacuna.counterapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.diegoacuna.counterapp.R
import cl.diegoacuna.counterapp.data.Counter


class CounterRecyclerViewAdapter(
    private val counters: MutableList<Counter>,
    val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CounterRecyclerViewAdapter.CustomViewHolder>() {


    fun setData(data: MutableList<Counter>) {
        counters.clear()
        counters.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.counter_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = counters.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(counters[position])
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(counter: Counter) {

            itemView.findViewById<TextView>(R.id.tv_title).text = counter.title
            itemView.findViewById<TextView>(R.id.tv_count).text = counter.count.toString()

            itemView.setOnLongClickListener {
                onItemListener.onDeleteItem(counter)
                false
            }
            itemView.findViewById<Button>(R.id.button_increase).setOnClickListener {

                onItemListener.onIncreaseCount(counter)
            }
            itemView.findViewById<Button>(R.id.button_decrease).setOnClickListener {
                onItemListener.onDecreaseCount(counter)
            }

        }
    }

    interface OnItemListener {
        fun onDeleteItem(counter: Counter)
        fun onIncreaseCount(counter: Counter)
        fun onDecreaseCount(counter: Counter)
    }
}