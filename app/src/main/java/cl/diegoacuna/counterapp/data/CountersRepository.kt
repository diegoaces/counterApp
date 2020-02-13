package cl.diegoacuna.counterapp.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cl.diegoacuna.counterapp.service.CounterService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountersRepository(application: Application) {
    private val counterDao: CounterDao? = CountersDatabase.getInstance(application)?.counterDao()

    fun insert(counter: Counter) {
        counterDao?.let {
            InsertAsyncTask(it).execute(counter)
        }
    }

    fun delete(counter: Counter) {
        counterDao?.let {
            DeleteAsyncTask(it).execute(counter)
            removeCountersOnline(counter)
        }
    }

    fun update(counter: Counter) {
        counterDao?.let {
            UpdateAsyncTask(it).execute(counter)
        }
    }

    fun getCounters(): LiveData<List<Counter>> {
        return counterDao?.getOrderedCounter() ?: MutableLiveData<List<Counter>>()
    }

    fun getCountersOnline() {
        val counterService: CounterService = CounterService.Factory.create()

        counterService.getCounters()
            .enqueue(object : Callback<List<Counter>> {
                override fun onFailure(call: Call<List<Counter>>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }

                override fun onResponse(
                    call: Call<List<Counter>>,
                    response: Response<List<Counter>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach { counterApi ->

                            counterApi.remote = 1
                            insert(counterApi)

                        }
                    }
                }
            })
    }

    private fun removeCountersOnline(counter: Counter) {
        val counterService: CounterService = CounterService.Factory.create()

        counterService.deleteCount(counter.id)
            .enqueue(object : Callback<List<LiveData<Counter>>> {
                override fun onFailure(call: Call<List<LiveData<Counter>>>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }

                override fun onResponse(
                    call: Call<List<LiveData<Counter>>>,
                    response: Response<List<LiveData<Counter>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach { counterApi ->

                            if (counterApi.value != null) {
                                delete(counterApi.value!!)
                            }
                        }
                    }
                }

            })

        //.enqueue(object : Callback<List<LiveData<Counter>>> {
        //    override fun onFailure(call: Call<List<LiveData<Counter>>>, t: Throwable) {
        //        Log.e("Error", t.message.toString())
        //    }
//
        //    override fun onResponse(
        //        call: Call<List<LiveData<Counter>>>,
        //        response: Response<List<LiveData<Counter>>>
        //    ) {
        //        if (response.isSuccessful) {
        //            response.body()?.forEach { counterApi ->
//
        //                val counter = Counter(counterApi.title, counterApi.count)
        //                counter.id = counterApi.id
        //                counter.remote = 1
        //                insert(counter)
        //            }
        //        }
        //    }

    }

    fun setCountersOnline(counter: Counter) {

        val counterService: CounterService = CounterService.Factory.create()

        if (counter.remote == 0) {

            counterService.createCounter(counter.title)
                .enqueue(object : Callback<List<LiveData<Counter>>> {
                    override fun onFailure(
                        call: Call<List<LiveData<Counter>>>,
                        t: Throwable
                    ) {
                        Log.e("Error", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<List<LiveData<Counter>>>,
                        response: Response<List<LiveData<Counter>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.forEach { counterApi ->

                                counterApi.value?.remote = 1
                                insert(counter)
                            }
                        }
                    }
                })
        }
    }

    private class InsertAsyncTask(private val counterDao: CounterDao) :
        AsyncTask<Counter, Void, Void>() {
        override fun doInBackground(vararg counters: Counter?): Void? {

            for (counter in counters) {
                counter?.let {
                    counterDao.insert(counter)
                }
            }

            return null
        }

    }

    private class DeleteAsyncTask(private val counterDao: CounterDao) :
        AsyncTask<Counter, Void, Void>() {
        override fun doInBackground(vararg counters: Counter?): Void? {

            for (counter in counters) {
                counter?.let {
                    counterDao.delete(counter)
                }
            }
            return null
        }
    }

    private class UpdateAsyncTask(private val counterDao: CounterDao) :
        AsyncTask<Counter, Void, Void>() {
        override fun doInBackground(vararg counters: Counter?): Void? {

            for (counter in counters) {
                counter?.let {
                    counterDao.update(counter)
                }
            }
            return null
        }
    }
}