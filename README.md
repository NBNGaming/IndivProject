# Crypto Rate
Индивидуальный проект для IT Школы Samsung. Автор - Батычко Николай Николаевич
## Материалы
1. [Исполняемый файл приложения](https://github.com/NBNGaming/IndivProject/releases/latest/download/app-debug.apk)
2. Исходный код приложения - этот репозиторий
3. Презентация о проекте - [PPTX](https://disk.yandex.ru/i/rzz5EzOMbdjRZg), [PDF](https://disk.yandex.ru/i/XZmZWnVbM-38jg)
4. [Демонстрационное видео](https://disk.yandex.ru/i/XSdO0wbyE30EAA)
## Описание
Приложение для отслеживания цен (курсов) криптовалют. В приложении можно:
- добавлять через поиск криптовалюты
- удалять криптовалюты
- смотреть цену, изменение цены в процентах, график курса и капитализацию
- обновлять данные о криптовалютах

Все данные кэшируются на внутреннем накопителе. Данные берутся из [CoinGecko API](https://www.coingecko.com/ru/api).

Использованы библиотеки:
- [Retrofit](https://github.com/square/retrofit) (для запросов к API)
- [Picasso](https://github.com/square/picasso) (для подгрузки изображений из сети)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) (для построения графика)
- [Gson](https://github.com/google/gson) (для использования с Retrofit и сериализации массивов при сохранении в базу данных)

## Сборка проекта
Чтобы проект успешно собрался, нужно в файле `local.properties` указать API-ключ CoinGecko.

Примером может служить файл `local.properties.sample`.
