package com.example.seminarku

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seminarku.R

@Composable
fun KartuKuota(
    namaKelas: String,
    jumlahPeserta: Int,
    kapasitas: Int,
    terpilih: Boolean,
    dapatDipilih: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val sisaKuota = (kapasitas - jumlahPeserta).coerceAtLeast(0)
    val warnaStatus = when {
        jumlahPeserta >= kapasitas -> MaterialTheme.colorScheme.error
        sisaKuota <= 2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    val teksStatus = when {
        jumlahPeserta >= kapasitas -> stringResource(R.string.class_full)
        sisaKuota <= 2 -> stringResource(R.string.class_almost_full)
        else -> stringResource(R.string.class_available)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = dapatDipilih, onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (terpilih) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (terpilih) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(namaKelas, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Text(
                stringResource(R.string.quota_fraction, jumlahPeserta, kapasitas, sisaKuota),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                teksStatus,
                color = warnaStatus,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
