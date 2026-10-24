package com.example.seminarku

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
fun biayaAnggotaTanpaSertifikatAdalahNol() {
    val peserta = Peserta("A", "1001", true, "Android", false)
    assertEquals(0, hitungBiaya(peserta))
}

@Test
fun biayaAnggotaDenganSertifikatAdalahSepuluhRibu() {
    val peserta = Peserta("B", "1002", true, "Android", true)
    assertEquals(10_000, hitungBiaya(peserta))
}

@Test
fun biayaNonanggotaTanpaSertifikatAdalahLimaBelasRibu() {
    val peserta = Peserta("C", "1003", false, "IoT", false)
    assertEquals(15_000, hitungBiaya(peserta))
}

@Test
fun biayaNonanggotaDenganSertifikatAdalahDuaPuluhLimaRibu() {
    val peserta = Peserta("D", "1004", false, "UI/UX", true)
    assertEquals(25_000, hitungBiaya(peserta))
}


