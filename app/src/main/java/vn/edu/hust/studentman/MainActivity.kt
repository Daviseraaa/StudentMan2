package vn.edu.hust.studentman

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private val students = mutableListOf(
    StudentModel("Nguyễn Thị Mai", "20220001"),
    StudentModel("Trần Văn Bình", "20220002"),
    StudentModel("Đỗ Minh Hiếu", "20220005"),
  )

  private val studentAdapter = StudentAdapter(students)

  private lateinit var root: ConstraintLayout

  private lateinit var customActivityLauncher: ActivityResultLauncher<Intent>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
      registerForContextMenu(this)
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    root = findViewById(R.id.main)

    customActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == RESULT_OK) {
          val name = res.data?.getStringExtra("name") ?: ""
          val id = res.data?.getStringExtra("id") ?: ""
          val state = res.data?.getStringExtra("state") ?: ""
          if (name.isBlank() || id.isBlank()) return@registerForActivityResult
          if (state == "add") {
            students.add(StudentModel(name, id))
            studentAdapter.notifyItemInserted(students.size - 1)
          } else if (state == "edit") {
            val pos = res.data?.getIntExtra("position", RecyclerView.NO_POSITION) ?: RecyclerView.NO_POSITION
            if (pos != RecyclerView.NO_POSITION) {
              students[pos] = StudentModel(name, id)
              studentAdapter.notifyItemChanged(pos)
            }
        }
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_options, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_add -> {
        val intent = Intent(this, AddActivity::class.java)
        intent.putExtra("state", "add")
        customActivityLauncher.launch(intent)
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }

  override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
    super.onCreateContextMenu(menu, v, menuInfo)
    menuInflater.inflate(R.menu.context_menu, menu)
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    val position = StudentAdapter.selectedPos

    val builder = AlertDialog.Builder(this)
      .setIcon(R.drawable.baseline_warning_amber_24)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete this student?")
      .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

    val dialog: AlertDialog = builder.setPositiveButton("Yes") { dialog, _ ->
      val pos: Int = position
      val student: StudentModel? = if (pos != RecyclerView.NO_POSITION) students[pos] else null
      if (pos != RecyclerView.NO_POSITION) {
        studentAdapter.removeStudent(pos)
        dialog.dismiss()
        Snackbar.make(root, "Student deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(pos, student!!)
            studentAdapter.notifyItemInserted(pos)
          }.show()
      }
    }.create()

    return when (item.itemId) {
      R.id.edit -> {
        val intent = Intent(this, AddActivity::class.java)
        intent.putExtra("name", students[position].studentName)
        intent.putExtra("id", students[position].studentId)
        intent.putExtra("state", "edit")
        intent.putExtra("position", position)
        customActivityLauncher.launch(intent)
        true
      }
      R.id.delete -> {
        dialog.show()
        true
      }
      else -> super.onContextItemSelected(item)
    }
  }
}