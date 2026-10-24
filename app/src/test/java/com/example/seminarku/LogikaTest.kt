package com.example.seminarku

import com.example.seminarku.model.Peserta
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LogikaTest {
    @Test
    fun biayaPendaftaranMengikutiStatusAnggotaDanSertifikat() {
        assertEquals(0, hitungBiaya(peserta(anggota = true, sertifikat = false)))
        assertEquals(10_000, hitungBiaya(peserta(anggota = true, sertifikat = true)))
        assertEquals(15_000, hitungBiaya(peserta(anggota = false, sertifikat = false)))
        assertEquals(25_000, hitungBiaya(peserta(anggota = false, sertifikat = true)))
    }

    @Test
    fun validasiNimMenolakKosongHurufDanNimDuplikat() {
        val daftar = listOf(peserta(nim = "12345"))

        assertEquals("NIM tidak boleh kosong", validasiNim("", daftar))
        assertEquals("NIM hanya boleh berisi angka", validasiNim("12A45", daftar))
        assertEquals("NIM sudah terdaftar", validasiNim("12345", daftar))
        assertNull(validasiNim("67890", daftar))
    }

    @Test
    fun hitungJumlahDanSisaKuotaPerKelas() {
        val daftar = listOf(
            peserta(nim = "1", kelas = "Android"),
            peserta(nim = "2", kelas = "Android"),
            peserta(nim = "3", kelas = "IoT")
        )

        assertEquals(2, hitungJumlahPeserta(daftar, "Android"))
        assertEquals(3, hitungSisaKuota(daftar, "Android"))
        assertEquals(KAPASITAS_PER_KELAS, hitungSisaKuota(daftar, "UI/UX"))
    }

    @Test
    fun ringkasanMenghitungPesertaDanaDanSertifikat() {
        val daftar = listOf(
            peserta(nim = "1", anggota = true, sertifikat = true),
            peserta(nim = "2", anggota = false, sertifikat = false),
            peserta(nim = "3", anggota = false, sertifikat = true)
        )

        assertEquals(3, hitungTotalPeserta(daftar))
        assertEquals(50_000, hitungTotalDana(daftar))
        assertEquals(2, hitungJumlahSertifikat(daftar))
    }

    @Test
    fun daftarKosongMemberiRingkasanNol() {
        assertEquals(0, hitungTotalPeserta(emptyList()))
        assertEquals(0, hitungTotalDana(emptyList()))
        assertEquals(0, hitungJumlahSertifikat(emptyList()))
        assertTrue(hitungSisaKuota(emptyList(), "Android") == KAPASITAS_PER_KELAS)
    }

    private fun peserta(
        nim: String = "12345",
        kelas: String = "Android",
        anggota: Boolean = true,
        sertifikat: Boolean = false
    ) = Peserta(
        nama = "Peserta Uji",
        nim = nim,
        anggotaHimpunan = anggota,
        kelas = kelas,
        sertifikatCetak = sertifikat
    )
}
