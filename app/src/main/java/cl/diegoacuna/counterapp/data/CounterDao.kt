package cl.diegoacuna.counterapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(counter: Counter)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg counter: Counter)

    @Delete
    fun delete(vararg contact: Counter)

    @Query("SELECT * FROM ${Counter.TABLE_NAME} ORDER BY counter_id")
    fun getOrderedCounter(): LiveData<List<Counter>>


    @Query("SELECT * FROM  ${Counter.TABLE_NAME}  WHERE id = :id")
    fun getCounterById(id: String): LiveData<List<Counter>>
}