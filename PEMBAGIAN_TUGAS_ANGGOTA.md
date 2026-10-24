# Panduan Tugas dan Commit SeminarKu

## Ringkasan Kasus

SeminarKu adalah aplikasi Android satu halaman berbasis Kotlin dan Jetpack Compose untuk mengelola pendaftaran seminar himpunan. Seluruh bagian disusun vertikal dalam satu layar yang dapat digulir: formulir dan kuota kelas, daftar peserta, lalu ringkasan.

Aturan yang harus tetap sama di semua bagian aplikasi:

- Data peserta terdiri dari nama, NIM, status anggota himpunan, kelas, dan pilihan sertifikat cetak.
- NIM wajib diisi, hanya berisi angka, dan tidak boleh sama dengan NIM peserta lain.
- Anggota himpunan membayar Rp0; bukan anggota membayar Rp15.000.
- Sertifikat cetak menambah biaya Rp10.000 untuk semua peserta.
- Kelas Android, IoT, dan UI/UX masing-masing menampung maksimal 5 peserta. Kelas penuh ditampilkan sebagai penuh dan tidak bisa dipilih.
- Ringkasan menampilkan jumlah peserta, sisa kuota setiap kelas, dana terkumpul, dan jumlah sertifikat cetak.
- Menghapus peserta mengurangi jumlah kelas tersebut dan memperbarui semua informasi ringkasan.

## Peta Kode Saat Ini

| Bagian | Lokasi | Isi utama |
|---|---|---|
| Model peserta | `app/src/main/java/com/example/seminarku/model/Peserta.kt` | `data class Peserta` |
| Aturan dan perhitungan | `app/src/main/java/com/example/seminarku/Logika.kt` | Validasi NIM, biaya, kuota, dana, dan sertifikat |
| Kartu kuota | `app/src/main/java/com/example/seminarku/KartuKuota.kt` | Status dan indikator kuota per kelas |
| Layar utama | `app/src/main/java/com/example/seminarku/MainActivity.kt` | State, formulir, kuota, daftar, ringkasan, composable bersama |
| Teks UI | `app/src/main/res/values/strings.xml` | Label, pesan, dan judul yang dipakai UI |
| Ikon | `app/src/main/res/drawable/ic_seminar.xml` | Ikon SeminarKu |
| Tes JVM | `app/src/test/java/com/example/seminarku/` | Tes logika Kotlin |

> `MainActivity.kt` saat ini memuat beberapa bagian sekaligus. Karena itu, jangan mengerjakan file tersebut secara bersamaan tanpa koordinasi. Tetapkan satu integrator atau gabungkan perubahan secara bergiliran agar perubahan anggota lain tidak tertimpa.

## Anggota 1 — Model Data dan Logika Kotlin

### Tanggung jawab

Menentukan bentuk data peserta serta mengimplementasikan aturan kasus sebagai fungsi Kotlin yang dapat dipakai UI. Logika bisnis tidak sebaiknya dihitung ulang secara berbeda di setiap composable.

### Kode yang dikerjakan

**`model/Peserta.kt`** — pastikan model memuat semua data pendaftaran:

```kotlin
data class Peserta(
    val nama: String,
    val nim: String,
    val anggotaHimpunan: Boolean,
    val kelas: String,
    val sertifikatCetak: Boolean
)
```

**`Logika.kt`** — implementasikan atau rapikan fungsi berikut:

```kotlin
const val KAPASITAS_PER_KELAS = 5

fun hitungBiaya(peserta: Peserta): Int = when {
    peserta.anggotaHimpunan && peserta.sertifikatCetak -> 10_000
    peserta.anggotaHimpunan -> 0
    peserta.sertifikatCetak -> 25_000
    else -> 15_000
}

fun validasiNim(nim: String, daftar: List<Peserta>): String? = when {
    nim.isBlank() -> "NIM tidak boleh kosong"
    !nim.all(Char::isDigit) -> "NIM hanya boleh berisi angka"
    daftar.any { it.nim == nim } -> "NIM sudah terdaftar"
    else -> null
}

fun hitungJumlahPeserta(daftar: List<Peserta>, kelas: String) =
    daftar.count { it.kelas == kelas }

fun hitungTotalDana(daftar: List<Peserta>) = daftar.sumOf(::hitungBiaya)
```

Lengkapi juga fungsi `hitungSisaKuota`, `hitungTotalPeserta`, dan `hitungJumlahSertifikat`. Gunakan `count`, `sumOf`, atau fungsi koleksi lain untuk perhitungan daftar. Pastikan penghapusan tidak memerlukan manipulasi kuota tersendiri: jumlah kuota harus berasal dari daftar peserta terkini.

### File yang di-commit

- `app/src/main/java/com/example/seminarku/model/Peserta.kt`
- `app/src/main/java/com/example/seminarku/Logika.kt`
- Tes logika yang ditambahkan di `app/src/test/java/com/example/seminarku/` bila dikerjakan bersama Anggota 5.

### Pemeriksaan dan commit

Pastikan fungsi dapat dipanggil dari package UI dan hasil biaya sesuai empat kombinasi anggota/sertifikat. Commit hanya perubahan milik bagian ini:

```bash
git add app/src/main/java/com/example/seminarku/model/Peserta.kt app/src/main/java/com/example/seminarku/Logika.kt
git commit -m "Add participant model and seminar rules"
```

## Anggota 2 — Formulir Masukan dan Validasi

### Tanggung jawab

Membangun bagian **formulir pendaftaran** pada halaman utama: nama, NIM, status keanggotaan, kelas, sertifikat, rincian biaya, serta tombol daftar. Formulir menerima daftar peserta dan event pendaftaran dari composable induk; jangan membuat salinan daftar lokal.

### Kode yang dikerjakan

Di `MainActivity.kt`, fokus pada composable `FormPendaftaran`:

- Nama wajib diisi; tampilkan pesan yang sesuai setelah kolom disentuh.
- Batasi masukan NIM menjadi angka dan gunakan `validasiNim` untuk pesan kosong/duplikat.
- Tampilkan pilihan kelas Android, IoT, UI/UX; pilihan kelas penuh harus nonaktif.
- Biaya langsung berubah mengikuti status anggota dan pilihan sertifikat.
- Tombol daftar `enabled` hanya ketika nama, NIM, dan kelas valid.
- Saat berhasil, kirim `Peserta` melalui callback. Pengosongan kolom dan reset pilihan tetap konsisten dengan rancangan UI.

Contoh bentuk API yang disarankan agar formulir tidak mengubah state daftar secara langsung:

```kotlin
@Composable
fun FormPendaftaran(
    daftarPeserta: List<Peserta>,
    kelas: List<String>,
    onDaftar: (Peserta) -> Unit
)
```

### File yang di-commit

- Perubahan terbatas pada `FormPendaftaran` di `app/src/main/java/com/example/seminarku/MainActivity.kt`.
- String baru di `app/src/main/res/values/strings.xml` hanya bila ada teks formulir/pesan yang memang belum tersedia.

Karena Anggota 3–5 juga mungkin menyentuh `MainActivity.kt`, koordinasikan patch atau merge bergiliran dan periksa diff sebelum commit.

### Pemeriksaan dan commit

Periksa bahwa tombol nonaktif untuk input invalid, NIM duplikat ditolak, kelas penuh tidak dapat dipilih, dan biaya sertifikat menambah Rp10.000. Commit:

```bash
git add app/src/main/java/com/example/seminarku/MainActivity.kt app/src/main/res/values/strings.xml
git commit -m "Build registration form and input validation"
```

## Anggota 3 — Daftar Peserta dan Penghapusan

### Tanggung jawab

Membuat bagian **daftar peserta** dan tampilan satu peserta. Aksi hapus harus dikirim ke induk melalui callback agar state tunggal diperbarui dan kuota/dana/sertifikat ikut berubah.

### Kode yang dikerjakan

Di `MainActivity.kt`, fokus pada composable `DaftarPeserta` dan `KartuPeserta`:

- Tampilkan jumlah peserta dan daftar kartu.
- Setiap kartu menampilkan nama, NIM, kelas, status anggota, pilihan sertifikat, dan biaya peserta.
- Tombol hapus memanggil callback; composable kartu tidak mengelola daftar sendiri.
- Jika daftar kosong, tampilkan pesan keadaan kosong yang sudah disediakan di string resource.

Contoh API:

```kotlin
@Composable
fun DaftarPeserta(
    daftarPeserta: List<Peserta>,
    onHapus: (Peserta) -> Unit
)
```

### File yang di-commit

- Perubahan composable `DaftarPeserta` dan `KartuPeserta` di `app/src/main/java/com/example/seminarku/MainActivity.kt`.
- `strings.xml` jika ada label daftar yang belum tersedia.

### Pemeriksaan dan commit

Daftarkan beberapa peserta, hapus salah satu, lalu pastikan data hilang dan kuota kelasnya tersedia lagi. Commit:

```bash
git add app/src/main/java/com/example/seminarku/MainActivity.kt app/src/main/res/values/strings.xml
git commit -m "Add participant list and delete action"
```

## Anggota 4 — Tata Letak, Sumber Daya, dan Ringkasan

### Tanggung jawab

Menjaga susunan visual dan sumber daya tetap konsisten serta membangun bagian **Ringkasan** di halaman yang sama. Gunakan teks dari `strings.xml`, jarak kelipatan 4 dp, dan komponen layout Compose yang sesuai.

### Kode yang dikerjakan

- Periksa layout bersama: `Column`, `Row`, `Box` bila diperlukan, `Modifier`, padding, ukuran, dan scroll.
- Kelola judul/label/pesan melalui `app/src/main/res/values/strings.xml`; hindari teks UI hard-coded.
- Gunakan ikon seminar dari `app/src/main/res/drawable/ic_seminar.xml` dan ikon/label navigasi yang sudah digunakan aplikasi.
- Buat atau rapikan `RingkasanSeminar` di `MainActivity.kt` untuk total peserta, sisa kuota tiap kelas, dana, dan jumlah sertifikat.
- Ambil angka dari fungsi di `Logika.kt`, bukan perhitungan berbeda yang ditulis ulang di tampilan.
- Letakkan ringkasan setelah daftar peserta pada halaman gulir dan jangan menambahkan fitur di luar kebutuhan kasus.

### File yang di-commit

- `app/src/main/java/com/example/seminarku/MainActivity.kt` (bagian layout/ringkasan yang disepakati).
- `app/src/main/res/values/strings.xml`.
- `app/src/main/res/drawable/` hanya bila ikon yang diperlukan memang perlu ditambah atau diperbaiki.

### Pemeriksaan dan commit

Pastikan ringkasan benar saat daftar kosong, setelah pendaftaran, dan setelah penghapusan. Periksa teks UI berasal dari resource. Commit:

```bash
git add app/src/main/java/com/example/seminarku/MainActivity.kt app/src/main/res/values/strings.xml app/src/main/res/drawable
git commit -m "Add seminar summary and organize screen resources"
```

Jika tidak mengubah drawable, jangan sertakan direktori itu di `git add`.

## Anggota 5 — State, Integrasi, dan Pengujian

### Tanggung jawab

Mengelola state peserta di composable induk, menghubungkan formulir, daftar, kuota, dan ringkasan dalam satu halaman, serta memeriksa alur kasus secara menyeluruh. Anggota ini menjadi integrator akhir agar hanya ada satu daftar peserta sebagai sumber data.

### Kode yang dikerjakan

- Di `PendaftaranSeminarScreen`, simpan daftar dengan state Compose, lalu teruskan sebagai data ke formulir, daftar, kuota, dan ringkasan.
- Hubungkan `onDaftar` untuk menambah peserta dan `onHapus` untuk menghapus peserta.
- Susun seluruh bagian berurutan di dalam satu `Column` yang dapat digulir; tidak perlu navigasi tab.
- Pastikan kuota, ringkasan, biaya, dan peserta memakai daftar yang sama.
- Tambahkan tes JVM pada `app/src/test/java/com/example/seminarku/` untuk aturan logika yang dapat dites tanpa perangkat.
- Lakukan pemeriksaan UI/manual untuk navigasi tab dan aliran tambah/hapus. Jangan menambahkan dependency pengujian atau fitur baru tanpa kebutuhan tugas.

Skenario pemeriksaan minimal:

1. NIM kosong dan nama kosong menahan pendaftaran.
2. NIM yang mengandung karakter selain angka ditolak; NIM terdaftar tidak dapat dipakai ulang.
3. Anggota tanpa sertifikat Rp0; anggota dengan sertifikat Rp10.000.
4. Bukan anggota tanpa sertifikat Rp15.000; bukan anggota dengan sertifikat Rp25.000.
5. Kelas menerima lima peserta dan pilihan menjadi nonaktif saat penuh.
6. Menghapus peserta mengurangi hitungan kelas dan mengaktifkan pilihan kelas kembali.
7. Jumlah peserta, sisa kuota, total dana, dan sertifikat pada ringkasan sesuai daftar.
8. Formulir dikosongkan setelah pendaftaran berhasil dan peserta tetap terlihat pada halaman yang sama.

### File yang di-commit

- `app/src/main/java/com/example/seminarku/MainActivity.kt` (state induk dan wiring callback pada satu halaman).
- Tes di `app/src/test/java/com/example/seminarku/`.
- Bila ada, hanya perubahan integrasi yang diperlukan pada file milik anggota lain dan sudah disepakati.

### Pemeriksaan dan commit

Jalankan dari root project:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

Catat hasil pemeriksaan di deskripsi pull request atau laporan kelompok. Commit:

```bash
git add app/src/main/java/com/example/seminarku/MainActivity.kt app/src/test
git commit -m "Connect seminar state and verify registration flows"
```

## Tahapan Kerja dan Urutan Commit Kelompok

Urutan berikut meminimalkan konflik dan menyusun bagian dari fondasi sampai integrasi:

1. **Anggota 1** menyelesaikan model dan fungsi aturan kasus, lalu commit `Add participant model and seminar rules`.
2. **Anggota 5** menyiapkan kerangka halaman utama dan state tunggal serta kontrak callback yang dibutuhkan. Jika UI belum siap, gunakan composable sementara yang sederhana; jangan membuat state peserta kedua.
3. **Anggota 2** menyambungkan formulir ke callback `onDaftar` dan commit formulir/validasinya.
4. **Anggota 3** menyambungkan daftar dan aksi hapus ke callback `onHapus`.
5. **Anggota 4** merapikan resources/layout halaman gulir dan melengkapi bagian ringkasan dengan fungsi Anggota 1.
6. **Anggota 5** mengintegrasikan perubahan, menambah/menjalankan pemeriksaan, lalu membuat commit integrasi jika ada perubahan miliknya.

Jika memakai satu branch bersama, hanya satu orang mengedit atau commit pada satu waktu. Sebelum commit, setiap anggota jalankan:

```bash
git status
git diff
```

Stage file yang menjadi tanggung jawabnya secara spesifik (hindari `git add .` agar file lokal/build tidak ikut). Setelah commit:

```bash
git log --oneline -5
```

Jika memakai branch terpisah, setiap anggota membuat branch dari branch kelompok terbaru, menyelesaikan bagian miliknya, lalu membuka pull request ke branch integrasi. Integrator menggabungkan pull request satu per satu sesuai urutan di atas, menyelesaikan konflik `MainActivity.kt`, kemudian menjalankan pemeriksaan akhir. Cantumkan commit masing-masing anggota pada laporan agar kontribusi dapat ditelusuri.

## Batasan agar Implementasi Tetap Konsisten

- Jangan membuat daftar/state peserta terpisah di composable bagian atau kartu.
- Jangan menggandakan aturan biaya dan kuota di UI bila fungsi logika sudah tersedia.
- Jangan menghapus peserta hanya dari tampilan; ubah state daftar di induk.
- Jangan mengaktifkan tombol daftar saat data tidak valid atau kelas terpilih penuh.
- Jangan menambah tab atau fitur di luar aturan Kasus 3 dan kebutuhan satu halaman.
- Setiap anggota harus memahami alur dari formulir → daftar peserta → kuota dan ringkasan, bukan hanya file yang menjadi bagiannya.
