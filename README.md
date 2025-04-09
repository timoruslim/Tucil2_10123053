# Tugas Kecil 2 | Strategi Algoritma (IF2211)

Ini adalah Tugas Kecil 2 mata kuliah Strategi Algoritma (IF2211) yang berupa program kompresi gambar menggunakan metode quadtree. Quadtree adalah struktur data pohon yang setiap simpulnya memiliki maksimal empat anak. Di sini, quadtree merepresentasikan pembagian suatu gambar menjadi blok-blok yang lebih kecil, sehingga dapat digunakan untuk mengkompresi gambar. Digunakan metode berbasis _divide and conquer_ untuk melakukan kompresi dengan quadtree.

## _Divide and Conquer_

Suatu gambar dapat dikompresi dengan membaginya menjadi empat blok. Lalu, setiap blok akan dibagikan terus-menerus secara rekursif. Jika suatu blok sudah dapat dihampirkan dengan warna rataannya dengan cukup baik (galat di bawah suatu ambang batas), atau blok tersebut sudah cukup kecil, pembagian akan dihentikan. Proses ini dapat dipermudahkan menggunakan data struktur quadtree, dengan setiap simpul merepresentasikan suatu blok dari gambar, lalu simpul daun menyatakan blok yang sudah dihampirkan dengan warna rataannya. Untuk informasi selengkapnya, dapat dilihat di laporan.

## Program Requirements

Program menggunakan bahasa Java, sehingga diperlukan hal berikut.

1. JDK (Java Development Kit)

## Dependencies

Program juga menggunakan dependency berikut.

1. `gif-sequence-writer.jar` ([library](https://gist.github.com/jesuino/528703e7b1974d857b36) untuk animasi GIF oleh Elliot Kroo)

## Build and Run Program

Berikut langkah-langkah untuk menjalankan program.

1. Clone repository ini.

   ```sh
   git clone https://github.com/timoruslim/Tucil2_10123053
   ```

2. Navigate to `Tucil2_10123053` directory.

   ```sh
   cd Tucil2_10123053
   ```

3. Compile java files to `bin`.

   ```sh
   javac -d bin -cp "lib/gif-sequence-writer.jar" src/*.java
   ```

4. Execute `App` with dependencies.

   ```sh
   java -cp "bin;lib/gif-sequence-writer.jar" App
   ```

Folder juga dapat dibuka dengan IDE sebagai project, lalu file `App.java` dapat di-run secara langsung.

## Struktur Program

Berikut struktur dari program ini.

```
├── bin/
│   ├── App.class
│   ├── ErrorCalculator.class
│   ├── GifSequenceWriter.class
│   ├── Quadtree.class
│   ├── QuadtreeNode.class
│   └── ThresholdCalculator.class
├── doc/
│   └── Tucil2_K1_10123053_Timothy Niels Ruslim.pdf
├── lib/
│   └── gif-sequence-writer.jar
├── src/
│   ├── App.java
│   ├── ErrorCalculator.java
│   ├── GifSequenceWriter.java
│   ├── Quadtree.java
│   ├── QuadtreeNode.java
│   └── ThresholdCalculator.java
├── test/
│   ├── bird.jpg
│   ├── boat.jpeg
│   ├── cabin.png
│   ├── flowers.jpg
│   ├── suit.jpeg
│   ├── timo.jpg
│   └── tram.png
└── README.md
```

## How It Works

Program kompresi gambar ini berupa CLI. Setelah menjalankan program, berikut masukan yang diperlukan.

1. Alamat dari gambar yang ingin dikompresi.
2. Metode perhitungan galat.
3. Ambang batas nilai galat untuk pembagian.
4. Ukuran minimum blok.
5. (Opsional) Target persentase kompresi.
6. Alamat dari gambar hasil kompresi.
7. (Opsional) Alamat dari GIF proses kompresi.

Lalu, program akan menghasilkan keluaran berikut.

1. Waktu eksekusi kompresi (dalam ms).
2. Ukuran gambar sebelum kompresi (dalam MB).
3. Ukuran gambar sesudah kompresi (dalam MB).
4. Persentase kompresi yang tercapai.
5. Kedalaman pohon quadtree.
6. Banyak simpul pada pohon quadtree.
7. Gambar hasil kompresi pada alamat yang ditentukan.
8. (Opsional) GIF proses kompresi pada alamat yang ditentukan.

Berikut contoh kompresi yang dilakukan program.

<p align="center">
   <img src="https://github.com/timoruslim/Tucil2_10123053/blob/main/test/boat.jpeg" width="49%">
   <img src="https://github.com/timoruslim/Tucil2_10123053/blob/main/test/boat_compressed.gif" width="49%"> 
</p>

## Author

[Timothy Niels Ruslim](https://github.com/timoruslim) (10123053)
