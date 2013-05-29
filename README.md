ant-android-res-concat
======================

Таск для Apache Ant для выполнения слияния ресурсов (папка "res") двух android-проектов.

Используется (на текущий момент) для слияния в один двух независимо разрабатываемых частей одного android-проекта.
Слияние использует разделение "пространства имен" проектов по префиксам, что требует дополнительного жесткого согласования используемых пространств имен между разработчиками.
Нечто похожее умеет делать (вроде бы) "New build system" (построеная на Gradle) - но экспериментировать с версией 0.3 - пока не хочется.

Пример использования - в "test/build.xml"

Таск поддерживает следующие параметры:
to - указание пути до каталога проекта, В который будет проводится слияние
prefixto - префикс имен, использующийся в вышеуказанном проекте
from - указание пути до каталога проекта, КОТОРЫЙ будет сливаться с проектов, указанным в "to"
prefixfrom - префикс имен, использующийся во "from" проекте.

Алгоритм работы таска:
1. Если во "from" находится xml-файл, который отсутствует в "to" - то он копируется в "to"
2. Если файл из "from" присутствует в "to" - то проводится процедура их объединения по следующим правилам:
2.1 Из документа "to" последовательно удаляются все элементы, имеющие атрибут "name", начинающиеся с prefixfrom
2.2 Последовательно обрабатываются элементы документа из "from" (для простоты будем обозначать элемент документа из "from" - item ,а документ из "to" - toDoc):
2.2.1. Если item не имеет атрибута name:
2.2.1.1 Проверяется наличие в toDoc такой же вершины (по nodeName)
2.2.1.2 Если такой вершины нет - она добавляется в toDoc. Если есть - то игнорируется (анализ продолжается на child-элементах)
2.2.2. Если item имеет атрибут name: 
2.2.2.1 Если name начинается с prefixto - оно игнорируется (что гарантирует, что, к примеру, значения в strings.xml проекта "to" не будут НИКОГДА скорректированы)
2.2.2.2 Если name начинается с prefixfrom - оно добавляется в toDoc
2.2.2.3 Если name не начинается ни с prefixto, ни с prefixfrom - то проверяется наличие такой вершины с таким name в toDoc. Если такой вершины нет - она добавляется. Если есть - игнорируется

Пример результатов работы таска:
------------------------------------------------
Buildfile: [...]\ant-android-res-concat\testData\build.xml

merge:
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_activity_main.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_activity_main.xml"
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialdic_listitem.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialdic_listitem.xml"
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialdic_main.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialdic_main.xml"
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialtype_edit.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialtype_edit.xml"
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\menu\main.xml" and "[...]\ant-android-res-concat\testData\2\res\menu\main.xml"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values\dimens.xml"
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_horizontal_margin']"
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_vertical_margin']"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\strings.xml" and "[...]\ant-android-res-concat\testData\2\res\values\strings.xml"
[android.res.merge] Remove /resources/string[@name='db_shortname']
[android.res.merge] Remove /resources/string[@name='db_materialtype']
[android.res.merge] Remove /resources/string[@name='db_material_dictionary']
[android.res.merge] Found duplicated node - "/resources/string[@name='app_name']"
[android.res.merge] Found duplicated node - "/resources/string[@name='action_settings']"
[android.res.merge] Found duplicated node - "/resources/string[@name='hello_world']"
[android.res.merge] Found duplicated node - "/resources/string[@name='material_kind_dictionary']"
[android.res.merge] Adding /resources/string[@name='db_shortname']
[android.res.merge] Adding /resources/string[@name='db_materialtype']
[android.res.merge] Adding /resources/string[@name='db_material_dictionary']
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values\styles.xml"
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Light']"
[android.res.merge] Found duplicated node - "/resources/style[@name='AppTheme'][@parent='AppBaseTheme']"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-sw600dp\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values-sw600dp\dimens.xml"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-sw720dp-land\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values-sw720dp-land\dimens.xml"
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_horizontal_margin']"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-v11\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values-v11\styles.xml"
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light']"
[android.res.merge] 
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-v14\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values-v14\styles.xml"
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light.DarkActionBar']"
[android.res.merge] 

BUILD SUCCESSFUL
Total time: 0 seconds

