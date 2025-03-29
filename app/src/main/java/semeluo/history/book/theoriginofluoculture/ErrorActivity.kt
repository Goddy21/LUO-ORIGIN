package semeluo.history.book.theoriginofluoculture

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error) // Ensure this layout file exists

        val errorMessage = intent.getStringExtra("ERROR_MESSAGE") ?: "An unknown error occurred."
        val errorTextView: TextView = findViewById(R.id.errorTextView)
        errorTextView.text = errorMessage

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Close ErrorActivity and return to the previous Activity
        }
    }
}
