package com.bcaf.asuransi_akses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClaimsAdapter(private val claimsList: List<Claim>) :
    RecyclerView.Adapter<ClaimsAdapter.ClaimViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_claim, parent, false)
        return ClaimViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claimsList[position]
        holder.bind(claim)
    }

    override fun getItemCount(): Int = claimsList.size

    class ClaimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        private val idKontrakTextView: TextView = itemView.findViewById(R.id.idKontrakTextView)
        private val jenisAsuransiTextView: TextView = itemView.findViewById(R.id.jenisAsuransiTextView)
        private val noPolisTextView: TextView = itemView.findViewById(R.id.noPolisTextView)
        private val namaCustomerTextView: TextView = itemView.findViewById(R.id.namaCustomerTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)

        fun bind(claim: Claim) {
            idTextView.text = "ID: ${claim.id}"
            idKontrakTextView.text = "ID Kontrak: ${claim.idKontrak.idKontrak}"
            noPolisTextView.text = "No Polis: ${claim.noPolis}"
            jenisAsuransiTextView.text = "Jenis Asuransi: ${formatInsuranceType(claim.jenisAsuransi)}"
            namaCustomerTextView.text = "Nama: ${claim.idCustomer.namaCustomer}"

            // Set Status Text
            statusTextView.text = when (claim.status) {
                1 -> "On Progress"
                2 -> "In Review"
                3 -> "Pending"
                4 -> "Approved"
                5 -> "Rejected"
                else -> "Unknown"
            }

            // Change Status Background Color
            val statusColor = when (claim.status) {
                1 -> android.graphics.Color.parseColor("#9E9E9E") // Yellow for Pending 9E9E9E
                2 -> android.graphics.Color.parseColor("#FF9800") // Orange for In Review
                3 -> android.graphics.Color.parseColor("#FF9800") // Green for Approved FF9800
                4 -> android.graphics.Color.parseColor("#4CAF50") // Red for Rejected 4CAF50
                5 -> android.graphics.Color.parseColor("#F44336") // Grey for Completed F44336
                else -> android.graphics.Color.parseColor("#2196F3") // Default Blue
            }
            statusTextView.setBackgroundColor(statusColor)
        }

        // Format jenis asuransi
        private fun formatInsuranceType(type: String): String {
            val formattedTypes = mapOf(
                "asuransiKendaraan" to "Asuransi Kendaraan",
                "creditProtection" to "Credit Protection",
                "personalAccident" to "Personal Accident",
                "carProtection" to "Car Protection"
            )
            return formattedTypes[type] ?: type // Jika tidak ditemukan, kembalikan tipe asli
        }
    }
}

