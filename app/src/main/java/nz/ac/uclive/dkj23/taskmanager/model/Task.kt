package nz.ac.uclive.dkj23.taskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Entity data class representing the Task table
 */

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val name: String,
    val description: String,
    val dueDate: String,
    val priority: String,
) {

}