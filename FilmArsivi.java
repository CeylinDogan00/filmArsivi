package com.filmarsivi;

// --- YENİ IMPORTLAR (Dosya işlemleri için) ---
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// --- Mevcut importlar ---
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bu sınıf, Film nesnelerini yönetmek için kullanılır (Servis Sınıfı).
 * Filmleri bir listede tutar, ekler, siler, filtreler.
 * AÇILIŞTA: filmleri dosyadan yükler.
 * KAPANIŞTA: filmleri dosyaya kaydeder.
 */
public class FilmArsivi {

    private List<Film> filmListesi;

    // YENİ: Kayıt dosyasının adını bir sabit (constant) olarak tanımlıyoruz.
    // Bu dosya, projenizin ana dizininde (root) oluşacaktır.
    private static final String DOSYA_ADI = "filmler_arsivi.csv";

    // --- Constructor (Yapıcı Metot) - DÜZELTİLDİ ---
    /**
     * FilmArsivi oluşturulduğunda, listeyi başlatır ve
     * 'filmleriDosyadanYukle' metodunu çağırarak verileri yükler.
     */
    // HATA 1 DÜZELTİLDİ: 'public class FilmArsivi()' -> 'public FilmArsivi()' olarak değiştirildi.
    // Yapıcı metotlar (Constructor) 'class' anahtar kelimesini almaz.
    public FilmArsivi() {
        // 1. Listeyi boş olarak başlat
        this.filmListesi = new ArrayList<>();

        // 2. Listeyi dosyadan okuyarak doldurmaya çalış
        // HATA 2 DÜZELTİLDİ: 'filmleriDosYadanYukle()' -> 'filmleriDosyadanYukle()' (büyük Y harfi düzeltildi)
        filmleriDosyadanYukle();
    }

    // --- YENİ METOT: Dosyadan Yükleme ---
    /**
     * Program başladığında 'DOSYA_ADI' olarak belirtilen dosyayı okur.
     * Dosya yoksa (ilk çalıştırma), test verilerini oluşturur ve yeni bir dosya yaratır.
     * Dosya varsa, içindeki satırları okur, 'Film' nesnelerine dönüştürür
     * ve 'filmListesi'ne ekler.
     */
    private void filmleriDosyadanYukle() {
        File dosya = new File(DOSYA_ADI);

        // 1. Dosya var mı?
        if (!dosya.exists()) {
            // Dosya yoksa (ilk çalıştırma)
            System.out.println("Arşiv dosyası (" + DOSYA_ADI + ") bulunamadı. İlk test verileri oluşturuluyor...");
            // Test verilerini listeye ekle
            this.filmListesi.add(new Film("Yüzüklerin Efendisi", "Peter Jackson", "Fantastik", 2001, 8.8));
            this.filmListesi.add(new Film("Baba", "Francis Ford Coppola", "Suç", 1972, 9.2));
            this.filmListesi.add(new Film("Kara Şövalye", "Christopher Nolan", "Aksiyon", 2008, 9.0));
            // Ve bu test verilerini hemen yeni dosyaya kaydet
            filmleriDosyayaKaydet();
            return;
        }

        // 2. Dosya varsa, oku.
        // 'try-with-resources' bloğu, 'reader' nesnesinin
        // işlem bitince (veya hata olunca) otomatik kapanmasını sağlar.
        try (BufferedReader reader = new BufferedReader(new FileReader(dosya))) {
            String satir;
            // Dosyanın sonuna gelene kadar satır satır oku
            while ((satir = reader.readLine()) != null) {
                // CSV satırını (;) ayraçına göre parçala
                String[] parcalar = satir.split(";");

                // Satırın 5 parçadan oluştuğunu doğrula (Bozuk veriyi engelle)
                if (parcalar.length == 5) {
                    try {
                        // Parçaları doğru veri tiplerine çevir
                        String baslik = parcalar[0];
                        String yonetmen = parcalar[1];
                        String tur = parcalar[2];
                        int yapimYili = Integer.parseInt(parcalar[3]); // Hata olabilir
                        double puan = Double.parseDouble(parcalar[4]); // Hata olabilir

                        // Yeni film nesnesi oluştur ve listeye ekle
                        this.filmListesi.add(new Film(baslik, yonetmen, tur, yapimYili, puan));

                    } catch (NumberFormatException e) {
                        // Yıl veya Puan'ı sayıya çevirme hatası (dosya bozulmuşsa)
                        System.out.println("Hata: Arşiv dosyasında format hatası (sayısal veri): " + satir);
                        // Bu satırı atla ve devam et
                    }
                } else {
                    System.out.println("Uyarı: Arşiv dosyasında bozuk satır bulundu ve atlandı: " + satir);
                }
            }
            System.out.println(this.filmListesi.size() + " adet film arşivden başarıyla yüklendi.");

        } catch (IOException e) {
            // Dosya okuma hatası (örn: izin yok)
            System.out.println("Hata: Arşiv dosyası okunurken bir hata oluştu: " + e.getMessage());
        }
        // Not: NumberFormatException'ı try-catch içine alarak programın çökmesini engelledim,
        // böylece bozuk bir satırda bile diğer filmler yüklenmeye devam eder.
    }

    // --- YENİ METOT: Dosyaya Kaydetme ---
    /**
     * 'filmListesi'ndeki TÜM filmleri 'DOSYA_ADI'na yazar.
     * Bu metot, dosyanın üzerine yazar (overwrite).
     * Program kapatılırken 'Main' sınıfı tarafından çağrılır.
     */
    public void filmleriDosyayaKaydet() {
        // 'try-with-resources' bloğu, 'writer' nesnesinin
        // işlem bitince otomatik kapanmasını sağlar.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI))) {

            // Listeyi döngüye al
            for (Film film : this.filmListesi) {
                // Her film için CSV satırını al (Film.java'da eklediğimiz metot)
                String csvSatiri = film.toCsvString();
                // Satırı dosyaya yaz
                writer.write(csvSatiri);
                // Bir sonraki satıra geç
                writer.newLine();
            }
            System.out.println(this.filmListesi.size() + " adet film arşive başarıyla kaydedildi.");

        } catch (IOException e) {
            // Dosya yazma hatası (örn: disk dolu, izin yok)
            System.out.println("Hata: Arşiv dosyasına yazılırken bir hata oluştu: " + e.getMessage());
        }
    }


    // --- MEVCUT METOTLAR (Bu kısımlarda hata yoktu) ---

    public void filmEkle(Film film) {
        this.filmListesi.add(film);
        System.out.println("'" + film.getBaslik() + "' adlı film arşive başarıyla eklendi.");
        // Not: Hemen dosyaya kaydetmiyoruz, çıkışta toplu kaydedeceğiz.
    }

    public void filmleriListele() {
        if (this.filmListesi.isEmpty()) {
            System.out.println("-------------------------------------");
            System.out.println("Film arşivi şu anda boş.");
            System.out.println("-------------------------------------");
        } else {
            System.out.println("--- FİLM ARŞİVİ LİSTESİ ---");
            for (Film film : this.filmListesi) {
                System.out.println(film);
                try {
                    // Her film arasında kısa bir bekleme koyuyoruz (0.5 saniye)
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // hata olursa devam et
                }
            }
            System.out.println("-------------------------------------");
            System.out.println("Toplam " + filmListesi.size() + " adet film listelendi.");
            System.out.println("-------------------------------------");

            // Liste bittikten sonra kullanıcıdan Enter bekliyoruz
            System.out.println("Devam etmek için Enter'a bas...");
            try {
                System.in.read(); // Enter'a basılmasını bekler
            } catch (Exception e) {
                // hata olursa geç
            }
        }
    }


    public void filmSil(String filmBasligi) {
        boolean silindi = this.filmListesi.removeIf(film ->
                film.getBaslik().trim().equalsIgnoreCase(filmBasligi.trim())
        );

        if (silindi) {
            System.out.println("'" + filmBasligi + "' başlıklı film arşivden başarıyla silindi.");
        } else {
            System.out.println("Hata: '" + filmBasligi + "' başlıklı film arşivde bulunamadı.");
        }
    }

    public List<Film> filtreleTureGore(String tur) {
        return this.filmListesi.stream()
                .filter(film -> film.getTur().trim().equalsIgnoreCase(tur.trim()))
                .collect(Collectors.toList());
    }

    public List<Film> filtreleYilaGore(int yil) {
        return this.filmListesi.stream()
                .filter(film -> film.getYapimYili() == yil)
                .collect(Collectors.toList());
    }



    public List<Film> filtrelePuanaGore(double minPuan) {
        return this.filmListesi.stream()
                .filter(film -> film.getPuan() >= minPuan)
                .collect(Collectors.toList());
    }
}
