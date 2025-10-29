package com.filmarsivi;

import java.util.List; // Filtre sonuç listesi için
import java.util.Scanner;

/**
 * Uygulamanın ana giriş noktası (Entry Point).
 * Kullanıcı arayüzünü (konsol menüsü) yönetir.
 * Kullanıcıdan aldığı girdilere göre 'FilmArsivi' servis sınıfını kullanarak
 * ilgili işlemleri (Ekleme, Listeleme, Silme, Filtreleme) başlatır.
 *
 * Projenin son halidir (6. Adım dahil).
 */
public class Main {

    // Scanner'ı ve FilmArsivi'ni static olarak tanımlıyoruz ki
    // main ve diğer yardımcı metotlar erişebilsin.
    private static Scanner scanner = new Scanner(System.in);
    private static FilmArsivi filmArsivi = new FilmArsivi();

    /**
     * Java uygulamasının başladığı ana metot.
     * FilmArsivi nesnesi oluşturulduğunda (yukarıda),
     * filmler dosyadan otomatik olarak yüklenir.
     */
    public static void main(String[] args) {

        boolean calisiyor = true;

        // Ana uygulama döngüsü
        while (calisiyor) {

            menuyuGoster();
            System.out.print("Lütfen yapmak istediğiniz işlemin numarasını giriniz: ");
            String secim = scanner.nextLine();

            // Kullanıcının seçimine göre işlem yap
            switch (secim) {
                case "1":
                    filmEklemeIslemiYap();
                    break;
                case "2":
                    // DÜZELTİLDİ: Buradaki yazım hatası giderildi.
                    filmArsivi.filmleriListele();
                    break;
                case "3":
                    filmSilmeIslemiYap();
                    break;
                case "4":
                    filmFiltrelemeIslemiYap();
                    break;
                case "0":
                    // --- DOSYAYA KAYDETME ADIMI (6. ADIM) ---
                    // Program kapanmadan önce, hafızadaki güncel listeyi
                    // dosyaya kaydetmek için servis metodumuzu çağırıyoruz.
                    System.out.println("\nDeğişiklikler arşive kaydediliyor...");
                    filmArsivi.filmleriDosyayaKaydet();

                    System.out.println("Uygulamadan çıkılıyor. İyi günler!");
                    calisiyor = false; // Döngüyü sonlandır
                    break;
                default:
                    System.out.println("\nHata: Geçersiz seçim. Lütfen 0, 1, 2, 3 veya 4 giriniz.");
                    break;
            }
        }

        scanner.close(); // Program biterken Scanner kaynağını kapat
    }

    /**
     * Kullanıcıya ana menü seçeneklerini gösteren yardımcı metot.
     */
    private static void menuyuGoster() {
        System.out.println("\n--- FİLM ARŞİVİ YÖNETİM PANELİ ---");
        System.out.println("1. Yeni Film Ekle");
        System.out.println("2. Tüm Filmleri Listele");
        System.out.println("3. Film Sil");
        System.out.println("4. Filmleri Filtrele");
        System.out.println("0. Çıkış ve Kaydet");
        System.out.println("-------------------------------------");
    }

    /**
     * Kullanıcıdan film bilgilerini alıp ekleme işlemini yapar.
     * Sayısal girdiler için (yıl, puan) hata yönetimi (try-catch) içerir.
     */
    private static void filmEklemeIslemiYap() {
        System.out.println("\n--- Yeni Film Ekleme Ekranı ---");
        System.out.print("Film Başlığı: ");
        String baslik = scanner.nextLine();
        System.out.print("Yönetmen: ");
        String yonetmen = scanner.nextLine();
        System.out.print("Tür (Fantastik, Aksiyon, Suç vb.): ");
        String tur = scanner.nextLine();

        int yapimYili = 0;
        while (true) {
            try {
                System.out.print("Yapım Yılı: ");
                yapimYili = Integer.parseInt(scanner.nextLine());
                break; // Başarılıysa döngüden çık
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lütfen geçerli bir yıl (sayı) giriniz.");
            }
        }

        double puan = 0.0;
        while (true) {
            try {
                System.out.print("Puan (örn: 8.8): ");
                puan = Double.parseDouble(scanner.nextLine());
                break; // Başarılıysa döngüden çık
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lütfen geçerli bir puan (sayı) giriniz. Örn: 8.8");
            }
        }

        // Yeni Film nesnesini oluştur
        Film yeniFilm = new Film(baslik, yonetmen, tur, yapimYili, puan);
        // Arşive ekle
        filmArsivi.filmEkle(yeniFilm);
    }

    /**
     * Kullanıcıdan silinecek film başlığını alıp silme işlemini yapar.
     */
    private static void filmSilmeIslemiYap() {
        System.out.println("\n--- Film Silme Ekranı ---");
        System.out.print("Silmek istediğiniz filmin tam başlığını giriniz: ");
        String silinecekBaslik = scanner.nextLine();

        // Boş girdi kontrolü
        if (silinecekBaslik == null || silinecekBaslik.trim().isEmpty()) {
            System.out.println("Hata: Film başlığı boş olamaz. İşlem iptal edildi.");
            return; // Metottan çık
        }
        filmArsivi.filmSil(silinecekBaslik);
    }

    /**
     * Kullanıcıya filtreleme seçeneklerini (alt menü) sunar ve
     * seçime göre ilgili filtreleme işlemini başlatır.
     */
    private static void filmFiltrelemeIslemiYap() {
        System.out.println("\n--- Film Filtreleme Menüsü ---");
        System.out.println("1. Türe Göre Filtrele");
        System.out.println("2. Yapım Yılına Göre Filtrele");
        System.out.println("3. Puana Göre Filtrele (Minimum Puan)");
        System.out.println("0. Ana Menüye Dön");
        System.out.println("-------------------------------------");
        System.out.print("Seçiminiz: ");

        String secim = scanner.nextLine();
        List<Film> sonuclar; // Filtre sonuçlarını tutmak için liste

        switch (secim) {
            case "1":
                System.out.print("Aranacak film türünü giriniz: ");
                String tur = scanner.nextLine();
                sonuclar = filmArsivi.filtreleTureGore(tur);
                sonuclariYazdir(sonuclar); // Sonuçları yazdır
                break;

            case "2":
                int yil = 0;
                while (true) {
                    try {
                        System.out.print("Aranacak yapım yılını giriniz: ");
                        yil = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Hata: Lütfen geçerli bir yıl (sayı) giriniz.");
                    }
                }
                sonuclar = filmArsivi.filtreleYilaGore(yil);
                sonuclariYazdir(sonuclar);
                break;

            case "3":
                double minPuan = 0.0;
                while (true) {
                    try {
                        System.out.print("Minimum puanı giriniz (örn: 8.5): ");
                        minPuan = Double.parseDouble(scanner.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Hata: Lütfen geçerli bir puan (sayı) giriniz.");
                    }
                }
                sonuclar = filmArsivi.filtrelePuanaGore(minPuan);
                sonuclariYazdir(sonuclar);
                break;

            case "0":
                System.out.println("Ana menüye dönülüyor...");
                break;

            default:
                System.out.println("Hata: Geçersiz filtreleme seçimi.");
                break;
        }
    }

    /**
     * Parametre olarak aldığı bir film listesini (filtre sonuçlarını)
     * konsola formatlı bir şekilde yazdırır.
     *
     * @param sonuclar Yazdırılacak filmlerin listesi.
     */
    private static void sonuclariYazdir(List<Film> sonuclar) {
        if (sonuclar.isEmpty()) {
            System.out.println("\nBu kritere uygun hiçbir film bulunamadı.");
            return;
        }

        System.out.println("\n--- FİLTRELEME SONUÇLARI ---");

        for (Film film : sonuclar) {
            System.out.println(film);
            try {
                Thread.sleep(500); // her film arasında 0.5 saniye bekle
            } catch (InterruptedException e) {}
        }

        System.out.println("-------------------------------------");
        System.out.println("Toplam " + sonuclar.size() + " adet sonuç bulundu.");
        System.out.println("Devam etmek için Enter'a bas...");
        try {
            System.in.read(); // Enter bekle
        } catch (Exception e) {}

        System.out.println("-------------------------------------");
    }

} // Main sınıfının sonu