**SeminarKu — Aplikasi Pendaftaran Seminar Himpunan**

SeminarKu adalah aplikasi Android satu layar untuk membantu proses pendaftaran peserta seminar Himpunan Mahasiswa Teknologi Informasi. Aplikasi ini dibuat menggunakan Kotlin dan Jetpack Compose sebagai tugas Ujian Tengah Semester (UTS) mata kuliah Pemrograman Berbasis Mobile Tahun 2026.

Aplikasi mengintegrasikan formulir pendaftaran, pemilihan kelas, perhitungan biaya, informasi kuota, daftar peserta, dan ringkasan pendaftaran dalam satu layar.
Informasi Proyek
Informasi	Keterangan
Nama aplikasi	SeminarKu
Mata kuliah	Pemrograman Berbasis Mobile (PBM)
Tugas	UTS 2026
Kelompok	Kelompok 01
Kasus	Kasus 3 — Pendaftaran Seminar Himpunan
Platform	Android
Bahasa pemrograman	Kotlin
Framework antarmuka	Jetpack Compose
UI toolkit	Material 3
Application ID	com.example.seminarku
Repositori GitHub	uts-pbm-2026-kelompok-01
Video presentasi	Tonton di YouTube


Latar Belakang
Pendaftaran seminar secara manual dapat menyebabkan kesalahan pencatatan data peserta, perhitungan biaya, dan pemantauan kapasitas kelas. SeminarKu dibuat untuk membantu pengelolaan pendaftaran dalam satu antarmuka, sehingga pengguna dapat melihat data peserta serta perubahan kuota dan ringkasan secara langsung.
Tujuan Aplikasi
- Memudahkan pencatatan peserta seminar.
- Memvalidasi NIM agar hanya terdiri dari angka dan tidak terdaftar lebih dari satu kali.
- Menghitung biaya berdasarkan status keanggotaan dan pilihan sertifikat cetak.
- Membatasi jumlah peserta pada setiap kelas.
- Menyediakan daftar peserta yang dapat dihapus.
- Menampilkan ringkasan jumlah peserta, sisa kuota setiap kelas, total dana, dan jumlah sertifikat cetak.
Fitur Utama
1. Formulir pendaftaran
Pengguna mengisi nama lengkap dan NIM, memilih status keanggotaan, memilih kelas seminar, serta menentukan apakah memerlukan sertifikat cetak. Masukan yang tidak sesuai divalidasi dan ditandai dengan pesan kesalahan. Tombol pendaftaran tidak aktif ketika masukan wajib belum valid.
2. Perhitungan biaya otomatis
Biaya pendaftaran dan biaya sertifikat dihitung berdasarkan pilihan pengguna. Rincian biaya serta total biaya ditampilkan pada formulir dan diperbarui ketika pilihan diubah.
3. Kartu kuota kelas
Aplikasi menyediakan tiga kelas seminar: Android, IoT, dan UI/UX. Setiap kelas memiliki kapasitas maksimal lima peserta. Kartu kuota menampilkan jumlah peserta, kapasitas, sisa kuota, dan status kelas. Kelas yang telah penuh ditandai dan tidak dapat dipilih.
4. Daftar dan penghapusan peserta
Peserta yang berhasil didaftarkan muncul pada daftar peserta. Pengguna dapat menghapus peserta yang terdaftar. Perubahan daftar akan memengaruhi kuota kelas dan ringkasan seminar.
5. Ringkasan seminar
Ringkasan menampilkan total peserta, sisa kuota Android, IoT, dan UI/UX, total dana terkumpul, serta jumlah sertifikat yang harus dicetak. Nilai ringkasan dihitung dari data peserta terkini.
6. Keadaan kosong
Ketika belum ada peserta yang didaftarkan, aplikasi menampilkan pesan bahwa daftar masih kosong.
Aturan Bisnis
Komponen	Aturan
Nama peserta	Wajib diisi
NIM	Wajib diisi, hanya berisi angka, dan tidak boleh duplikat
Anggota himpunan	Biaya pendaftaran Rp0
Bukan anggota	Biaya pendaftaran Rp15.000
Sertifikat cetak	Tambahan Rp10.000 per peserta yang memilihnya
Kelas Android	Maksimal 5 peserta
Kelas IoT	Maksimal 5 peserta
Kelas UI/UX	Maksimal 5 peserta
Kelas penuh	Ditandai penuh dan tidak dapat dipilih
Penghapusan peserta	Mengembalikan kuota kelas terkait
Ringkasan	Dihitung dari daftar peserta terkini


Contoh perhitungan biaya
Status peserta	Sertifikat cetak	Total biaya
Anggota himpunan	Tidak	Rp0
Anggota himpunan	Ya	Rp10.000
Bukan anggota	Tidak	Rp15.000
Bukan anggota	Ya	Rp25.000


Rumus: Total biaya = biaya pendaftaran + biaya sertifikat cetak.
Teknologi yang Digunakan
- Kotlin — bahasa pemrograman aplikasi.
- Jetpack Compose — pembuatan antarmuka secara deklaratif.
- Material 3 — komponen dan gaya antarmuka.
- Android Studio — lingkungan pengembangan.
- Gradle Kotlin DSL — konfigurasi build proyek.
- JUnit 4 — pustaka pengujian unit yang digunakan pada konfigurasi proyek.
- Android SDK — perangkat pengembangan dan build aplikasi Android.
Konfigurasi Android
Konfigurasi	Nilai
Namespace	com.example.seminarku
Application ID	com.example.seminarku
minSdk	24
compileSdk	37
targetSdk	37
versionName	1.0


Struktur Proyek
SeminarKu/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/seminarku/
│   │   │   │   ├── model/
│   │   │   │   │   └── Peserta.kt
│   │   │   │   ├── ui/theme/
│   │   │   │   ├── KartuKuota.kt
│   │   │   │   ├── Logika.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   └── ic_seminar.xml
│   │   │   │   └── values/
│   │   │   │       └── strings.xml
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/wrapper/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── .gitignore
├── PEMBAGIAN_TUGAS_ANGGOTA.md
└── README.md
Berkas utama
Berkas	Fungsi
Peserta.kt	Mendefinisikan model data peserta menggunakan data class Peserta.
Logika.kt	Menyimpan fungsi perhitungan biaya, validasi NIM, jumlah peserta, kuota, total dana, dan sertifikat.
KartuKuota.kt	Menampilkan kartu kuota dan status ketersediaan kelas.
MainActivity.kt	Mengatur layar utama, formulir, state, daftar peserta, penghapusan, dan ringkasan.
strings.xml	Menyimpan teks antarmuka, label, status, dan pesan validasi.
ic_seminar.xml	Resource ikon seminar yang digunakan dalam antarmuka.


Cara Menjalankan Proyek
Persyaratan
- Android Studio versi stabil yang mendukung konfigurasi proyek.
- Android SDK Platform 37 sesuai pengaturan compileSdk proyek.
- Emulator Android atau perangkat Android.
- Koneksi internet ketika Gradle perlu mengunduh plugin atau dependensi yang belum tersedia.
1. Clone repositori
git clone https://github.com/zamm13/uts-pbm-2026-kelompok-01.git
cd uts-pbm-2026-kelompok-01
2. Buka proyek
1. Buka Android Studio.
2. Pilih Open dan pilih folder hasil clone.
3. Tunggu proses Gradle Sync selesai.
4. Pastikan Android SDK yang diperlukan sudah tersedia.
3. Jalankan aplikasi
Pilih emulator atau perangkat Android yang terhubung, lalu tekan tombol Run di Android Studio.
Build melalui terminal
Di Windows PowerShell atau Command Prompt:
.\gradlew.bat assembleDebug
Untuk menjalankan unit test di Windows:
.\gradlew.bat testDebugUnitTest
Di macOS atau Linux, gunakan ./gradlew assembleDebug dan ./gradlew testDebugUnitTest. Jalankan perintah tersebut untuk memeriksa hasil build dan pengujian pada lingkungan masing-masing.
Skenario Pengujian Fungsional
Tabel berikut berisi skenario yang perlu diperiksa terhadap perilaku aplikasi. Hasil aktual sebaiknya dicatat berdasarkan pengujian yang benar-benar dilakukan.
No.	Skenario	Hasil yang diharapkan
1	Membuka aplikasi tanpa peserta	Pesan keadaan kosong tampil dan ringkasan menunjukkan nilai awal.
2	Mendaftarkan anggota tanpa sertifikat	Pendaftaran berhasil dengan biaya Rp0.
3	Mendaftarkan anggota dengan sertifikat	Total biaya Rp10.000.
4	Mendaftarkan bukan anggota tanpa sertifikat	Total biaya Rp15.000.
5	Mendaftarkan bukan anggota dengan sertifikat	Total biaya Rp25.000.
6	Mengirim formulir dengan nama atau NIM kosong	Pendaftaran ditolak dan pesan validasi muncul.
7	Mengisi NIM dengan huruf	NIM ditolak karena hanya angka yang diizinkan.
8	Menggunakan NIM yang sudah terdaftar	Pendaftaran ditolak karena NIM duplikat.
9	Mendaftarkan peserta pada kelas yang tersedia	Daftar peserta, kuota, dan ringkasan berubah sesuai data baru.
10	Menghapus peserta	Peserta terhapus dan kuota serta ringkasan diperbarui.
11	Mencapai kapasitas lima peserta pada satu kelas	Kelas ditandai penuh dan tidak dapat dipilih.


Pembagian Tanggung Jawab
Berikut identitas seluruh anggota kelompok. Riwayat kontribusi kode dapat dilihat pada commit masing-masing di repositori GitHub.
No.	Nama	NIM	Akun GitHub
1	Hesti Mustika	2305903040001	@HestiMustika
2	Aura Nazwa	2305903040014	@Aura25
3	Muammar Huzaifi	2305903040024	@muammarhuzaifi-dotcom
4	Syifa Assirri	2305903040032	@sirri-byte
5	Muhammad Aizam Fahriza Limbong	2305903040054	@zamm13


Pembagian teknis tugas mengikuti tanggung jawab anggota yang tercatat pada berkas [`PEMBAGIAN_TUGAS_ANGGOTA.md`](PEMBAGIAN_TUGAS_ANGGOTA.md).
Tautan Proyek dan Presentasi
- Repositori GitHub: https://github.com/zamm13/uts-pbm-2026-kelompok-01
- Video presentasi YouTube: https://youtu.be/6jdVwIlRnWg
Video presentasi memuat demonstrasi aplikasi, penjelasan kode oleh anggota kelompok, modifikasi kode secara langsung, dan penunjukan hasil perubahan pada aplikasi.
Batasan Aplikasi
SeminarKu merupakan aplikasi pembelajaran yang berfokus pada dasar Kotlin, antarmuka Jetpack Compose, interaksi pengguna, dan pengelolaan state.
- Data peserta dikelola dalam state aplikasi dan tidak disimpan secara permanen ke database.
- Data pendaftaran dapat hilang ketika aplikasi ditutup.
- Aplikasi tidak menyediakan pembayaran daring atau server backend.
Batasan ini sesuai dengan cakupan tugas UTS PBM yang berfokus pada implementasi aplikasi Android satu layar.
Penutup
SeminarKu menerapkan formulir pendaftaran, validasi data, perhitungan biaya, pengelolaan kuota, daftar peserta, serta ringkasan seminar dalam satu aplikasi Android. Proyek ini menjadi praktik penerapan Kotlin, Jetpack Compose, komponen antarmuka, dan state untuk membangun aplikasi interaktif sesuai Kasus 3 — Pendaftaran Seminar Himpunan.
