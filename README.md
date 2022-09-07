### README

##Инструкция по обновлению словаря (excel-файл)
1) Очистить 4й столбец
2) Добавить новые слова (новые строки)
3) Для обновления перевода/транскрипции обновить значение в соответствующей ячейке, в 4й ячейке поставить символ "u" (update)
4) Для удаления слова в 4й ячейке поставить символ "r" (remove)
5) Прибавить 1 к значению Dictionary.EXCEL_FILE_VERSION
6) Поменять версию приложения в build.gradle в поле versionName

## Шансы выпадения слов
- Стандартный режим (40% - серая зона, 25% - красная зона, 25% - желтая зона, 10% - зеленая зона)
- Режим изучения нового (60% - серая зона, 40% - красная зона)
- Режим повторения (70% - желтая зона, 30% - зеленая зона)

## Разное
- Тонкая полоска над областью с переводом и транскрипцией отображает текущий цвет отображаемого слова

### ROAD MAP  

## Возможные фичи
- Подумать, как динамически определять время отображения SplashActivity
- Изучение по категориям (животные, транспорт, неправильные глаголы и т.д.) - подумать как это должно выглядеть -
        изучение в рамках категории, либо отображения список слов, попадающих под категорию и т.д.
- Обратный словарь (с русского на английский) - подумать формат и нужно ли вообще?
- Переписать проект на Kotlin
- Поиск слова
- Редактирование слова
- Избавиться от EXCEL_FILE_VERSION