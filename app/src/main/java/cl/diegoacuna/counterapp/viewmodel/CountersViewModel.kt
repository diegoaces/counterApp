package cl.diegoacuna.counterapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cl.diegoacuna.counterapp.data.Counter
import cl.diegoacuna.counterapp.data.CountersRepository

class CountersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CountersRepository(application)

    init {
        getAllFromServer()
    }

    val counters = repository.getCounters()
    val counter = repository.getCounters().value?.sumBy { it.count }

    fun saveCounter(counter: Counter) {
        repository.insert(counter)
    }

    private fun getAllFromServer() {
        repository.getCountersOnline()
    }


    fun deleteCounter(counter: Counter) {
        repository.delete(counter)
    }

    fun updateCounter(counter: Counter) {
        repository.update(counter)
    }
}