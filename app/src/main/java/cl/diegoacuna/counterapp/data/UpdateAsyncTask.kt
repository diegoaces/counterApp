package cl.diegoacuna.counterapp.data

import android.os.AsyncTask

class UpdateAsyncTask(private val counterDao: CounterDao) :
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