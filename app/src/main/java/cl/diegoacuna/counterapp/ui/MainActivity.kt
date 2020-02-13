package cl.diegoacuna.counterapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.diegoacuna.counterapp.R
import cl.diegoacuna.counterapp.data.Counter
import cl.diegoacuna.counterapp.databinding.ActivityMainBinding
import cl.diegoacuna.counterapp.viewmodel.CountersViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var countersViewModel: CountersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countersViewModel = ViewModelProvider(this).get(CountersViewModel::class.java)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this,
                R.layout.activity_main
            )

        binding.countersViewModel = countersViewModel
        binding.lifecycleOwner = this

        counter_rv.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = CounterRecyclerViewAdapter(mutableListOf(),

                object : CounterRecyclerViewAdapter.OnItemListener {
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
