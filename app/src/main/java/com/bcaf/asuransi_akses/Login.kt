package com.bcaf.asuransi_akses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiService = ApiClient.createService(ApiService::class.java)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                authenticateUser(username, password)
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://10.10.13.31:8080/api/auth/login")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonInputString = """
                {
                    "usernameOrNip": "$username",
                    "password": "$password"
                }
            """

                val outputStream: OutputStream = connection.outputStream
                outputStream.write(jsonInputString.toByteArray())
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStreamReader = InputStreamReader(connection.inputStream)
                    val response = inputStreamReader.readText()

                    withContext(Dispatchers.Main) {
                        // Tambahkan validasi untuk respons JSON di sini
                        val jsonResponse = JSONObject(response)
                        if (jsonResponse.has("jwt")) {
                            val token = jsonResponse.getString("jwt")

                            // Simpan token di SharedPreferences
                            val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("jwt_token", token)
                                apply()
                            }

                            // Navigasi ke Dashboard
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Tampilkan error jika key 'jwt' tidak ditemukan
                            Toast.makeText(
                                applicationContext,
                                "Login failed: Missing jwt in response",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Login Failed: $responseCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
