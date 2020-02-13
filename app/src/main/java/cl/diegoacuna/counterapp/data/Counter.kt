package cl.diegoacuna.counterapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*


@Entity(tableName = Counter.TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Counter(
    @ColumnInfo(name = "title") @NotNull val title: String,
    @ColumnInfo(name = "count") @NotNull var count: Int

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter_id")
    var counterId: Int = 0

    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString()

    @ColumnInfo(name = "is_remote")
    var remote: Int = 0

    companion object {
        const val TABLE_NAME = "counter"
    }
}