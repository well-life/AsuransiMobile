package com.bcaf.asuransi_akses

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        apiService = ApiClient.createService(ApiService::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchClaimsData { claimsList ->
            recyclerView.adapter = ClaimsAdapter(claimsList)
        }
    }

    private fun fetchClaimsData(callback: (List<Claim>) -> Unit) {
        lifecycleScope.launch {
            try {
                val token = PreferenceHelper.getToken(this@DashboardActivity)
                if (token.isNullOrEmpty()) {
                    throw IllegalStateException("Token is null or empty")
                }

                val claimsList = apiService.getClaims("Bearer $token")
                if (claimsList.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DashboardActivity, "No claims found", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                Log.d("CLAIMS_RESPONSE", claimsList.toString())
                callback(claimsList)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FETCH_CLAIMS_ERROR", "Error fetching claims: ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DashboardActivity, "Error fetching claims: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
