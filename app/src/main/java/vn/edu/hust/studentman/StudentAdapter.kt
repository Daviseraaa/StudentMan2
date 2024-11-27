package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
  private val students: MutableList<StudentModel>
): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  companion object {
    var selectedPos: Int = -1
  }

  inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    private val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)

    init {
      itemView.setOnLongClickListener {
        selectedPos = adapterPosition
        false
      }
    }

    fun bind(student: StudentModel) {
      textStudentName.text = student.studentName
      textStudentId.text = student.studentId
    }
  }

  fun removeStudent(position: Int) {
    if (position < 0 || position >= students.size) return
    students.removeAt(position)
    notifyItemRemoved(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
      R.layout.layout_student_item,
      parent, false
    )
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.bind(student)
  }
}