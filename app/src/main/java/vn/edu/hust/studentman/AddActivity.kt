package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_update_student)

        val name: String = intent.getStringExtra("name") ?: ""
        val id: String = intent.getStringExtra("id") ?: ""
        val state: String = intent.getStringExtra("state") ?: ""

        if (name.isNotBlank()) {
            findViewById<EditText>(R.id.edit_student_name).setText(name)
        }

        if (id.isNotBlank()) {
            findViewById<EditText>(R.id.edit_student_id).setText(id)
        }

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btn_update).text = if (state == "add") "Add" else "Update"

        findViewById<Button>(R.id.btn_update).setOnClickListener {
            val newName = findViewById<EditText>(R.id.edit_student_name).text.toString()
            val newID = findViewById<EditText>(R.id.edit_student_id).text.toString()
            intent.putExtra("name", newName)
            intent.putExtra("id", newID)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}