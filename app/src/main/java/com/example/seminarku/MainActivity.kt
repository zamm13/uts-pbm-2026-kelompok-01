package com.example.seminarku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seminarku.model.Peserta
import com.example.seminarku.ui.theme.SeminarKuTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SeminarKuTheme { PendaftaranSeminarScreen() } }
    }
}

@Composable
fun PendaftaranSeminarScreen(modifier: Modifier = Modifier) {
    val kelas = listOf(
        stringResource(R.string.class_android),
        stringResource(R.string.class_iot),
        stringResource(R.string.class_uiux)
    )
    val peserta = remember { mutableStateListOf<Peserta>() }
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderSeminar()
            FormPendaftaran(kelas, peserta)
            DaftarPeserta(daftarPeserta = peserta, onHapus = { peserta.remove(it) })
            RingkasanSeminar(daftarPeserta = peserta, kelas = kelas)
        }
    }
}

@Composable
private fun FormPendaftaran(kelas: List<String>, daftarPeserta: androidx.compose.runtime.snapshots.SnapshotStateList<Peserta>) {
    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var anggota by remember { mutableStateOf(true) }
    var kelasDipilih by remember { mutableStateOf(kelas.first()) }
    var sertifikat by remember { mutableStateOf(false) }
    var namaDisentuh by remember { mutableStateOf(false) }
    var nimDisentuh by remember { mutableStateOf(false) }
    var pesanSukses by remember { mutableStateOf(false) }
    val nimError = validasiNim(nim, daftarPeserta)
    val namaValid = nama.isNotBlank()
    val kelasPenuh = hitungJumlahPeserta(daftarPeserta, kelasDipilih) >= KAPASITAS_PER_KELAS
    val valid = namaValid && nimError == null && !kelasPenuh
    val biayaDasar = if (anggota) 0 else 15_000
    val biayaSertifikat = if (sertifikat) 10_000 else 0

    SectionTitle(stringResource(R.string.section_registration), stringResource(R.string.registration_subtitle))
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = nama, onValueChange = { nama = it; namaDisentuh = true; pesanSukses = false },
                modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.label_name)) },
                singleLine = true, isError = namaDisentuh && !namaValid,
                supportingText = { if (namaDisentuh && !namaValid) Text(stringResource(R.string.error_name)) },
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = nim, onValueChange = { nim = it.filter(Char::isDigit); nimDisentuh = true; pesanSukses = false },
                modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.label_nim)) },
                singleLine = true, isError = nimDisentuh && nimError != null,
                supportingText = { if (nimDisentuh && nimError != null) Text(errorNimString(nimError!!)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(12.dp)
            )
            Text(stringResource(R.string.label_membership), fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = anggota, onClick = { anggota = true })
                Text(stringResource(R.string.member))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = !anggota, onClick = { anggota = false })
                Text(stringResource(R.string.non_member))
            }
            Text(stringResource(R.string.label_class), fontWeight = FontWeight.SemiBold)
            kelas.forEach { item ->
                val full = hitungJumlahPeserta(daftarPeserta, item) >= KAPASITAS_PER_KELAS
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = kelasDipilih == item, enabled = !full, onClick = { kelasDipilih = item })
                    Text(item)
                    if (full) Text("  ·  ${stringResource(R.string.class_full)}", color = MaterialTheme.colorScheme.error)
                }
            }
            if (kelas.all { hitungJumlahPeserta(daftarPeserta, it) >= KAPASITAS_PER_KELAS }) {
                Text(stringResource(R.string.error_all_classes_full), color = MaterialTheme.colorScheme.error)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = sertifikat, onCheckedChange = { sertifikat = it })
                Text(stringResource(R.string.label_certificate))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            BiayaRow(stringResource(R.string.label_fee), formatRupiah(biayaDasar))
            BiayaRow(stringResource(R.string.label_certificate_fee), formatRupiah(biayaSertifikat))
            BiayaRow(stringResource(R.string.label_total), formatRupiah(biayaDasar + biayaSertifikat), emphasized = true)
            Button(
                onClick = {
                    if (valid) {
                        daftarPeserta.add(Peserta(nama.trim(), nim, anggota, kelasDipilih, sertifikat))
                        nama = ""; nim = ""; anggota = true; sertifikat = false
                        namaDisentuh = false; nimDisentuh = false; pesanSukses = true
                    }
            }, enabled = valid,
                modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)
            ) { Text(stringResource(R.string.button_register), fontWeight = FontWeight.Bold) }
            if (pesanSukses) Text(stringResource(R.string.registration_success), color = MaterialTheme.colorScheme.primary)
        }
    }
    SectionTitle(stringResource(R.string.section_quota), stringResource(R.string.quota_subtitle))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        kelas.forEach { item ->
            KartuKuota(item, hitungJumlahPeserta(daftarPeserta, item), KAPASITAS_PER_KELAS,
                kelasDipilih == item, hitungJumlahPeserta(daftarPeserta, item) < KAPASITAS_PER_KELAS,
                Modifier.weight(1f), onClick = { kelasDipilih = item })
        }
    }
}

@Composable
private fun HeaderSeminar() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Image(painterResource(R.drawable.ic_seminar), stringResource(R.string.seminar_icon_description), Modifier.size(42.dp))
        Column {
            Text(stringResource(R.string.app_name), color = MaterialTheme.colorScheme.secondary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            Text(stringResource(R.string.header_caption), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
    Text(stringResource(R.string.seminar_title), color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun BiayaRow(label: String, amount: String, emphasized: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = if (emphasized) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal)
        Text(amount, color = if (emphasized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
    }
}



@Composable
private fun KartuPeserta(peserta: Peserta, onHapus: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(peserta.nama, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                TextButton(onClick = onHapus) { Text(stringResource(R.string.button_delete), color = MaterialTheme.colorScheme.error) }
            }
            Text(stringResource(R.string.participant_nim, peserta.nim), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(R.string.participant_class, peserta.kelas), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(if (peserta.anggotaHimpunan) R.string.member else R.string.non_member), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(if (peserta.sertifikatCetak) R.string.certificate_selected else R.string.certificate_not_selected), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(R.string.participant_fee, formatRupiah(hitungBiaya(peserta))), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RingkasanSeminar(daftarPeserta: List<Peserta>, kelas: List<String>) {
    SectionTitle(stringResource(R.string.section_summary), stringResource(R.string.summary_subtitle))
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BiayaRow(stringResource(R.string.total_participants), stringResource(R.string.people_count, hitungTotalPeserta(daftarPeserta)))
            kelas.forEach { namaKelas ->
                val sisa = hitungSisaKuota(daftarPeserta, namaKelas)
                BiayaRow(stringResource(R.string.remaining_quota, namaKelas), stringResource(R.string.quota_fraction, KAPASITAS_PER_KELAS - sisa, KAPASITAS_PER_KELAS, sisa))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            BiayaRow(stringResource(R.string.total_funds), formatRupiah(hitungTotalDana(daftarPeserta)), emphasized = true)
            BiayaRow(stringResource(R.string.total_certificates), stringResource(R.string.certificate_count, hitungJumlahSertifikat(daftarPeserta)))
        }
    }
}

@Composable
private fun errorNimString(error: String): String = when (error) {
    "NIM tidak boleh kosong" -> stringResource(R.string.error_nim_empty)
    "NIM hanya boleh berisi angka" -> stringResource(R.string.error_nim_digits)
    else -> stringResource(R.string.error_nim_duplicate)
}

private fun formatRupiah(jumlah: Int): String = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(jumlah)
