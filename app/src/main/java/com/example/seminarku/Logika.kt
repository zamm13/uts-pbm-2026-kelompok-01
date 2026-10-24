package com.example.seminarku

import com.example.seminarku.model.Peserta

const val KAPASITAS_PER_KELAS = 5

fun hitungBiaya(peserta: Peserta): Int {
    val biayaPendaftaran = if (peserta.anggotaHimpunan) 0 else 15_000
    val biayaSertifikat = if (peserta.sertifikatCetak) 10_000 else 0
    return biayaPendaftaran + biayaSertifikat
}

fun validasiNim(nim: String, daftarPeserta: List<Peserta>): String? = when {
    nim.isBlank() -> "NIM tidak boleh kosong"
    !nim.all(Char::isDigit) -> "NIM hanya boleh berisi angka"
    daftarPeserta.any { it.nim == nim } -> "NIM sudah terdaftar"
    else -> null
}

fun hitungJumlahPeserta(daftarPeserta: List<Peserta>, kelas: String): Int =
    daftarPeserta.count { it.kelas == kelas }

fun hitungSisaKuota(daftarPeserta: List<Peserta>, kelas: String): Int =
    (KAPASITAS_PER_KELAS - hitungJumlahPeserta(daftarPeserta, kelas)).coerceAtLeast(0)

fun hitungTotalPeserta(daftarPeserta: List<Peserta>): Int = daftarPeserta.size

fun hitungTotalDana(daftarPeserta: List<Peserta>): Int =
    daftarPeserta.sumOf(::hitungBiaya)

fun hitungJumlahSertifikat(daftarPeserta: List<Peserta>): Int =
    daftarPeserta.count { it.sertifikatCetak }
