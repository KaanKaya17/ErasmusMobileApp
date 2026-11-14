# BlueSteps content and translation

[**Türkçe**](#türkçe) · [**English**](#english)

## Türkçe

Arayüz metinleri `app/src/main/res/values/strings.xml` ve aynı adlı `values-*` dil klasörlerindedir. İngilizce varsayılan dildir. Türkçe için `tr`, Portekizce için `pt`, Yunanca için `el`, Bulgarca için `bg` kullanılır.

Eğitim ve quiz verileri [`app/src/main/res/raw/`](../app/src/main/res/raw) ve buna karşılık gelen `raw-*` dil klasörlerindedir.

- `fish_data.json` ülkelere göre deniz canlılarını içerir.
- `sea_data.json` denizleri, fiziksel özelliklerini ve ekosistemlerini içerir.
- `questions.json` soruları, cevap seçeneklerini ve açıklamaları içerir.

Bir çeviriyi düzenlerken JSON alan adlarını ve kayıt kimliklerini koru. Quiz seçeneklerini yeniden sıralarsan sıfırdan başlayan `correct_answer_index` alanını yeni sıraya göre güncelle.

Android 13 veya üzeri bir cihazda ya da emülatörde ilgili dili ana ekrandan seç. Değiştirdiğin bilgi kartını aç veya quiz sorusunu yanıtla. Görsel veya 3B model bağlantısı değiştiyse internet bağlantısıyla açıldığını kontrol et. [Derleme adımları](../README.md#geliştiriciler-için)

## English

UI strings live in `app/src/main/res/values/strings.xml` and the matching `values-*` locale directories. English is the default. The locale codes are `tr` for Turkish, `pt` for Portuguese, `el` for Greek, and `bg` for Bulgarian.

Educational content and quizzes live in [`app/src/main/res/raw/`](../app/src/main/res/raw) and the matching `raw-*` locale directories.

- `fish_data.json` groups marine species by country.
- `sea_data.json` contains sea profiles, physical characteristics, and ecosystems.
- `questions.json` contains quiz questions, answer choices, and explanations.

Keep JSON field names and record IDs when editing translations. When reordering quiz answers, update the zero-based `correct_answer_index` to match the new order.

Select the edited language on the home screen of an Android 13+ device or emulator. Open the updated profile or answer the changed quiz question. For a changed image or 3D model URL, check that it loads with an internet connection. [Build instructions](../README.md#development)
