package cl.diegoacuna.counterapp.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.diegoacuna.counterapp.R
import cl.diegoacuna.counterapp.data.Counter
import cl.diegoacuna.counterapp.databinding.ActivityMainBinding
import cl.diegoacuna.counterapp.ui.CounterRecyclerViewAdapter.OnItemListener
import cl.diegoacuna.counterapp.viewmodel.CountersViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var countersViewModel: CountersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countersViewModel = ViewModelProvider(this).get(CountersViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        binding.countersViewModel = countersViewModel

        binding.lifecycleOwner = this

        counter_rv.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            adapter = CounterRecyclerViewAdapter(mutableListOf(),

                object : OnItemListener {
                    override fun onDeleteItem(counter: Counter) {
                        countersViewModel.deleteCounter(counter)
                    }

                    override fun onIncreaseCount(counter: Counter) {
                        counter.count = counter.count + 1
                        countersViewModel.updateCounter(counter)
                    }

                    override fun onDecreaseCount(counter: Counter) {
                        counter.count = counter.count - 1
                        countersViewModel.updateCounter(counter)
                    }

                })

            btnAdd.setOnClickListener {
                addCounter()
            }
        }
    }

    private fun addCounter() {
        countersViewModel.saveCounter(Counter("OK", 0))
    }


}

@BindingAdapter("data")
fun setRecyclerViewProperties(recyclerView: RecyclerView?, data: MutableList<Counter>?) {
    val adapter = recyclerView?.adapter

    if (adapter is CounterRecyclerViewAdapter && data != null) {
        adapter.setData(data)
    }
}
