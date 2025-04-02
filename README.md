# Tugas Kecil 2 | Strategi Algoritma (IF2211)

Ini adalah Tugas Kecil 2 mata kuliah Strategi Algoritma (IF2211) yang berupa program kompresi gambar menggunakan metode quadtree. Quadtree adalah struktur data pohon setiap simpulnya memiliki maksimal empat anak. Di sini, quadtree merepresentasikan pembagian suatu gambar menjadi blok-blok yang lebih kecil, sehingga dapat digunakan untuk mengkompresi gambar. Digunakan metode berbasis _divide and conquer_ untuk melakukan kompresi dengan quadtree.

## Program Requirements

Program menggunakan bahasa Java, sehingga diperlukan hal berikut.

1. JDK (Java Development Kit)

## Build and Run Program

Berikut langkah-langkah untuk menjalankan program.

1. Clone repository ini.

```bash
git clone https://github.com/timoruslim/Tucil2_10123053
```

3. Navigate to `src` directory.

```bash
cd Tucil2_10123053/src
```

4. Compile `App.java`.

```bash
javac App.java
```

3. Execute App.

```bash
java App
```

Folder juga dapat dibuka dengan IDE sebagai project, lalu file `App.java` dapat di-run secara langsung.

## Struktur Program

```bash
├── bin/
│   ├── App.class
│   ├── ErrorCalculator.class
│   ├── GifSequenceWriter.class
│   ├── Quadtree.class
│   ├── QuadtreeNode.class
│   └── ThresholdCalculator.class
├── doc/
│   └── Tucil2_K1_10123053_Timothy Niels Ruslim.pdf
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

## Authors

1. [Timothy Niels Ruslim (10123053)](https://github.com/timoruslim)
