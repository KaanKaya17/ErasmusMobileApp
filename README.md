# BlueSteps — We BLUE Erasmus+

Denizleri keşfet, deniz canlılarını tanı, öğrendiklerini quiz ile pekiştir.

**[📲 Android için APK indir](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest/download/BlueSteps.apk)** · **[Sürümler ve indirme dosyaları](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest)**

**Android 13 ve üzeri** · Türkçe, İngilizce, Portekizce, Yunanca ve Bulgarca

[Türkçe](#türkçe) · [English](#english)

## Türkçe

### Proje hakkında

BlueSteps (telefondaki adıyla **Blue Steps**), **We BLUE Erasmus+ projesi** kapsamında geliştirilen bir Android eğitim uygulamasıdır. Türkiye, Portekiz, Yunanistan ve Bulgaristan üzerinden denizleri ve deniz yaşamını tanıtır. Deniz ekosistemleri, bu ekosistemleri tehdit eden etkenler ve doğayı korumanın önemi hakkında farkındalık oluşturmayı amaçlar.

Uygulama; öğrenciler, proje katılımcıları ve deniz yaşamına ilgi duyan herkes için bilgi sayfalarını, görselleri ve etkileşimli soruları bir araya getirir.

### Telefona nasıl kurulur?

1. Android telefonunda **[APK indir](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest/download/BlueSteps.apk)** bağlantısına dokun. GitHub hesabı gerekmez.
2. İndirilen **BlueSteps.apk** dosyasını aç.
3. Telefon izin isterse, dosyayı açtığın tarayıcı veya Dosyalar uygulaması için **“Bu kaynaktan izin ver”** seçeneğini etkinleştir. Menü adı telefona göre değişebilir.
4. **Yükle** düğmesine dokun, ardından **Blue Steps** uygulamasını aç.
5. Ana ekrandaki dil menüsünden tercih ettiğin dili seç.

> Gereksinim: **Android 13 (API 33) veya üzeri**. APK Android içindir; iPhone/iOS üzerinde kurulamaz. İndirme başlamazsa [son sürüm sayfasındaki](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest) **Assets** bölümünden **BlueSteps.apk** dosyasını seç. “Source code” dosyaları uygulamanın kurulum dosyası değildir.

### Uygulamada neler var?

- **Ülkelere göre keşif:** Türkiye, Portekiz, Yunanistan ve Bulgaristan'a ait deniz canlılarını incele.
- **Canlı bilgi kartları:** Türlerin adları, yaşam alanları, dağılımları, fiziksel özellikleri ve açıklamaları hakkında bilgi edin.
- **Denizleri tanı:** Denizlerin konumları, derinlikleri, ekosistemleri ve öne çıkan türleri hakkında içerikleri oku.
- **Görsel galeriler:** Deniz ve canlı görselleriyle bilgileri destekle.
- **3B model görüntüleme:** Model bağlantısı bulunan canlıları uygulama içindeki görüntüleyicide keşfet.
- **Açıklamalı quiz:** Çoktan seçmeli soruları yanıtla; doğru cevabı ve açıklamasını gör.
- **Beş dil:** Türkçe, İngilizce, Portekizce, Yunanca ve Bulgarca arayüz ve içerik kaynakları.

Metinler ve quiz soruları uygulamanın kaynaklarında yer alır. Çevrimiçi görsellerin yüklenmesi ve 3B modellerin görüntülenmesi için internet bağlantısı gerekir; bazı görseller daha önce önbelleğe alınmışsa çevrimdışı görüntülenebilir.

### İndirme ve sürüm bilgisi

- **Son sürüm:** [GitHub Releases](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest)
- **Doğrudan APK:** [BlueSteps.apk](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest/download/BlueSteps.apk)
- **İlk paylaşılan APK:** 1.0 (`versionCode: 1`), yaklaşık 25 MiB.
- **Paket adı:** `com.example.bluesteps`

Paylaşılan 1.0 APK, Android debug sertifikasıyla imzalanmış ve hata ayıklama açık bir derlemedir. Dosya, sağlanan APK'nin adı düzenlenmiş birebir kopyasıdır. Dosya özeti sürüm açıklamasında ve `SHA256SUMS.txt` ekinde bulunur.

### Geliştiriciler için

Proje **Java**, Android XML arayüzleri ve Gradle Kotlin DSL kullanır. Görseller Glide, galeriler ViewPager2, 3B içerikler WebView üzerinden gösterilir. Çok dilli metinler `values-*`, eğitim ve quiz verileri `raw-*` kaynak klasörlerinde tutulur.

1. Depoyu klonla ve Android Studio'da aç.
2. **JDK 17** ve **Android SDK Platform 36** kurulu olduğundan emin ol; Gradle eşitlemesini tamamla.
3. Android 13 veya üzeri bir cihaz/emülatör seçip uygulamayı çalıştır.

Windows üzerinde geliştirme APK'sı oluşturmak için:

```powershell
.\gradlew.bat assembleDebug
```

macOS / Linux üzerinde:

```sh
./gradlew assembleDebug
```

Çıktı: `app/build/outputs/apk/debug/app-debug.apk`. Yerel derlemenin imzası paylaşılan APK'den farklı olabilir.

Hata veya öneri bildirmek için [Issues](https://github.com/KaanKaya17/ErasmusMobileApp/issues) bölümüne cihaz modelini, Android sürümünü ve sorunun adımlarını ekleyebilirsin.

## English

### About the project

**BlueSteps**, displayed as **Blue Steps** on Android, is an educational app developed as part of the **We BLUE Erasmus+ project**. It introduces marine life and seas associated with Türkiye, Portugal, Greece and Bulgaria, with the aim of raising awareness of marine ecosystems, environmental threats and the importance of protecting nature.

Students, project participants and anyone interested in the sea can explore species profiles, habitats, sea information and image galleries, view available 3D models, and reinforce their learning through multiple-choice questions with answer explanations. The app includes English, Turkish, Portuguese, Greek and Bulgarian language resources.

### Download and install

1. Open **[Download Android APK](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest/download/BlueSteps.apk)** on your Android phone. No GitHub account is needed.
2. Open **BlueSteps.apk** from your downloads.
3. If prompted, allow installation from the browser or file manager opening the APK.
4. Tap **Install**, open **Blue Steps**, and choose your preferred language on the home screen.

**Requires Android 13 (API 33) or later.** This APK does not install on iPhone/iOS. If the direct link does not start a download, open the [latest release](https://github.com/KaanKaya17/ErasmusMobileApp/releases/latest) and choose **BlueSteps.apk** under **Assets**. The “Source code” archives are for developers.

Text and quiz data are bundled with the app. Remote images and 3D models require internet access; previously cached images may remain available offline.

The shared 1.0 APK is a debuggable build signed with an Android debug certificate. It is the supplied APK with a clearer filename; its SHA-256 checksum is available in the release notes and the attached `SHA256SUMS.txt`.

### Development

Open the project in Android Studio with **JDK 17** and **Android SDK Platform 36**, sync Gradle, and run on an Android 13+ device or emulator. The app uses Java, XML layouts, Gradle Kotlin DSL, Glide, ViewPager2 and WebView. Build a development APK using `gradlew.bat assembleDebug` on Windows or `./gradlew assembleDebug` on macOS/Linux.

For bugs and suggestions, use [GitHub Issues](https://github.com/KaanKaya17/ErasmusMobileApp/issues) and include your device, Android version and steps to reproduce.
