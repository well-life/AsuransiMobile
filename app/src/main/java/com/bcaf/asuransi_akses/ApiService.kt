package com.bcaf.asuransi_akses

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class LoginRequest(val usernameOrNip: String, val password: String)
data class LoginResponse(val jwt: String)

data class Claim(
    val id: Int,
    val jenisAsuransi: String,
    val createdAt: String,
    val noPolis: Long,
    val status: Int,
    val modifiedAt: String?,
    val idCustomer: Customer,
    val idKontrak: Contract,
    val notes: String?,
    val description: String?
)

data class Customer(
    val idCustomer: Int,
    val namaCustomer: String,
    val alamat: String,
    val noTelepon: String,
    val email: String,
    val tanggalLahir: String,
    val jenisKelamin: String,
    val noKtp: String
)

data class Contract(
    val idKontrak: Int,
    val keterangan: String,
    val namaKendaraan: String?,
    val nomorPolisi: String?,
    val noPolis: String?,
    val idCustomer: Customer
)

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/claims/all")
    suspend fun getClaims(@Header("Authorization") token: String): List<Claim>
}
