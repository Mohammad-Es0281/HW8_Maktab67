package ir.es.mohammad.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import ir.es.mohammad.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var editTexts: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            if (getSharedPreferences("UserInfo", MODE_PRIVATE).contains("Username"))
                btnShowInfo.isEnabled = true
            editTexts = editTextGroup.referencedIds.map { findViewById(it) }
            editTexts.forEach { editText ->
                editText.addTextChangedListener {
                    addTextChangeListener(
                        editText
                    )
                }
            }

            radioGroupGender.setOnCheckedChangeListener { _, _ ->
                if (checkIsFormFilled())
                    btnRegister.isEnabled = true
            }

            btnRegister.setOnClickListener { setOnClickRegister() }

            btnShowInfo.setOnClickListener {
                textGroup.visibility = View.VISIBLE
                it.isEnabled = false
            }

            btnHideInfo.setOnClickListener {
                textGroup.visibility = View.GONE
                btnShowInfo.isEnabled = true
            }
        }
    }

    private fun addTextChangeListener(editText: EditText) {
        if (editText.text != null && editText.text.isNotBlank()) {
            if (checkIsFormFilled())
                binding.btnRegister.isEnabled = true
        }
    }

    private var isRegisterClicked = false
    private fun setOnClickRegister() {
        isRegisterClicked = true
        with(binding) {
            textFullName.text = "Full Name: ${editTextFullName.text}"
            textUsername.text = "Username: ${editTextUsername.text}"
            textEmail.text = "Email: ${editTextEmail.text}"
            textPassword.text = "Password: ${editTextTextPassword.text}"
            textGender.text = if (radioButtonFemale.isChecked) "Gender: Female" else "Gender: Male"

            Toast.makeText(this@MainActivity, "Done!", Toast.LENGTH_SHORT).show()
            if (btnHideInfo.visibility != View.VISIBLE)
                btnShowInfo.isEnabled = true
            btnRegister.isEnabled = false
        }
    }

    private fun checkIsFormFilled(): Boolean {
        if (binding.radioGroupGender.checkedRadioButtonId == -1)
            return false
        var isAllFill = true
        editTexts.forEach() { editText ->
            if (!(editText.text != null && editText.text.isNotBlank()))
                isAllFill = false
        }
        return isAllFill
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            getSharedPreferences("UserInfo", MODE_PRIVATE).run {
                textFullName.text = getString("Full name", "Full name: ")
                textUsername.text = getString("Username", "Username: ")
                textEmail.text = getString("Email", "Email: ")
                textPassword.text = getString("Password", "Password: ")
                textGender.text = getString("Gender", "Gender: ")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isRegisterClicked) {
            getSharedPreferences("UserInfo", MODE_PRIVATE).run {
                with(edit()) {
                    putString("Full name", binding.textFullName.text.toString())
                    putString("Username", binding.textUsername.text.toString())
                    putString("Email", binding.textEmail.text.toString())
                    putString("Password", binding.textPassword.text.toString())
                    putString("Gender", binding.textGender.text.toString())
                    apply()
                }
            }
        }
    }
}