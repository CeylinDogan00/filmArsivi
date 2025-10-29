package com.filmarsivi;

/**
 * Bu sınıf, bir filmin temel özelliklerini (niteliklerini) tutan model sınıfıdır.
 * POJO (Plain Old Java Object) veya "Data Class" olarak da bilinir.
 * Projemizin temel veri yapısıdır.
 */
public class Film {

    // --- Değişkenler (Fields) ---

    // Kapsülleme (Encapsulation) ilkesi gereği, sınıfın değişkenleri
    // 'private' olarak tanımlanır. Bu, değişkenlere dışarıdan doğrudan
    // erişilmesini engeller ve sadece metotlar (getter/setter) aracılığıyla
    // kontrollü erişim sağlar.
    private String baslik;
    private String yonetmen;
    private String tur;
    private int yapimYili;
    private double puan;

    // --- Constructor (Yapıcı Metot) ---

    /**
     * Film sınıfından yeni bir nesne (instance) oluşturulduğunda çalışan metottur.
     * Bu metot, bir film oluşturulurken tüm bilgilerin verilmesini zorunlu kılar.
     *
     * IntelliJ İpucu: Bu bloğu hızlıca oluşturmak için sınıf içinde
     * Alt + Insert (Windows/Linux) veya Cmd + N (Mac) tuşuna basın ve 'Constructor' seçin.
     */
    public Film(String baslik, String yonetmen, String tur, int yapimYili, double puan) {
        this.baslik = baslik;       // 'this.baslik', bu sınıfa ait 'baslik' değişkenidir.
        this.yonetmen = yonetmen;   // 'yonetmen', metottan parametre olarak gelendir.
        this.tur = tur;
        this.yapimYili = yapimYili;
        this.puan = puan;
    }

    // --- Getter ve Setter Metotları ---

    /**
     * 'private' değişkenlere kontrollü erişim (okuma ve yazma) sağlayan metotlar.
     *
     * IntelliJ İpucu: Bu bloğun tamamını Alt+Insert (Cmd+N) > "Getter and Setter"
     * seçeneği ile otomatik olarak oluşturabilirsiniz.
     */

    // baslik için
    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    // yonetmen için
    public String getYonetmen() {
        return yonetmen;
    }

    public void setYonetmen(String yonetmen) {
        this.yonetmen = yonetmen;
    }

    // tur için
    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    // yapimYili için
    public int getYapimYili() {
        return yapimYili;
    }

    public void setYapimYili(int yapimYili) {
        this.yapimYili = yapimYili;
    }

    // puan için
    public double getPuan() {
        return puan;
    }

    public void setPuan(double puan) {
        this.puan = puan;
    }


    // --- toString() Metodu ---

    /**
     * Bu metot, Film nesnesinin doğrudan ekrana yazdırılmaya çalışılması durumunda
     * (örn: System.out.println(filmNesnesi)) nasıl bir çıktı vereceğini belirler.
     * Eğer bu metodu yazmazsak, 'Film@1a2b3c4d' gibi anlamsız bir hafıza adresi yazdırılır.
     *
     * IntelliJ İpucu: Alt+Insert (Cmd+N) > "toString()" ile otomatik oluşturabilirsiniz.
     */
    @Override
    public String toString() {
        // Filmleri listelerken kullanacağımız format:
        return "Film {" +
                "Baslik='" + baslik + '\'' +
                ", Yonetmen='" + yonetmen + '\'' +
                ", Tur='" + tur + '\'' +
                ", Yil=" + yapimYili +
                ", Puan=" + puan +
                '}';
    }
    /**
     * Film nesnesinin verilerini, CSV (Noktalı Virgülle Ayrılmış) formatında
     * tek bir satır (String) olarak döndürür.
     * Bu metot, filmleri dosyaya yazarken kullanılacaktır.
     * Format: Baslik;Yonetmen;Tur;YapimYili;Puan
     *
     * @return CSV formatında film bilgisi içeren String.
     */
    public String toCsvString() {
        // Ayraç olarak noktalı virgül (;) kullanıyoruz.
        String ayirac = ";";
        return this.baslik + ayirac +
                this.yonetmen + ayirac +
                this.tur + ayirac +
                this.yapimYili + ayirac +
                this.puan;
    }
}