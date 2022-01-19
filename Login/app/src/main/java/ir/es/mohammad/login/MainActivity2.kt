package ir.es.mohammad.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<ConstraintLayout>(R.id.constraintLayout).setOnClickListener {
            Toast.makeText(
                this,
                "Toast",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}