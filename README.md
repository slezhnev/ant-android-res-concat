ant-android-res-concat
======================

Таск для Apache Ant для выполнения слияния ресурсов (папка "res") двух android-проектов. <br/>

Используется (на текущий момент) для слияния в один двух независимо разрабатываемых частей одного android-проекта.<br/>
Слияние использует разделение "пространства имен" проектов по префиксам, что требует дополнительного жесткого согласования используемых пространств имен между разработчиками.<br/>
Нечто похожее умеет делать (вроде бы) "New build system" (построеная на Gradle) - но экспериментировать с версией 0.3 - пока не хочется.<br/>

Пример использования - в "test/build.xml"

Таск поддерживает следующие параметры:<br/>
to - указание пути до каталога проекта, В который будет проводится слияние<br/>
prefixto - префикс имен, использующийся в вышеуказанном проекте<br/>
from - указание пути до каталога проекта, КОТОРЫЙ будет сливаться с проектов, указанным в "to"<br/>
prefixfrom - префикс имен, использующийся во "from" проекте.<br/>

Алгоритм работы таска:<br/>
1. Если во "from" находится xml-файл, который отсутствует в "to" - то он копируется в "to"<br/>
2. Если файл из "from" присутствует в "to" - то проводится процедура их объединения по следующим правилам:<br/>
2.1 Из документа "to" последовательно удаляются все элементы, имеющие атрибут "name", начинающиеся с prefixfrom<br/>
2.2 Последовательно обрабатываются элементы документа из "from" (для простоты будем обозначать элемент документа из "from" - item ,а документ из "to" - toDoc):<br/>
2.2.1. Если item не имеет атрибута name:<br/>
2.2.1.1 Проверяется наличие в toDoc такой же вершины (по nodeName)<br/>
2.2.1.2 Если такой вершины нет - она добавляется в toDoc. Если есть - то игнорируется (анализ продолжается на child-элементах)<br/>
2.2.2. Если item имеет атрибут name: <br/>
2.2.2.1 Если name начинается с prefixto - оно игнорируется (что гарантирует, что, к примеру, значения в strings.xml проекта "to" не будут НИКОГДА скорректированы)<br/>
2.2.2.2 Если name начинается с prefixfrom - оно добавляется в toDoc<br/>
2.2.2.3 Если name не начинается ни с prefixto, ни с prefixfrom - то проверяется наличие такой вершины с таким name в toDoc. Если такой вершины нет - она добавляется. Если есть - игнорируется<br/>

Пример результатов работы таска:<br/>
------------------------------------------------<br/>
Buildfile: [...]\ant-android-res-concat\testData\build.xml

merge:<br/>
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_activity_main.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_activity_main.xml"<br/>
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialdic_listitem.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialdic_listitem.xml"<br/>
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialdic_main.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialdic_main.xml"<br/>
[android.res.merge] Copying "[...]\ant-android-res-concat\testData\2\res\layout\db_materialtype_edit.xml" to "[...]\ant-android-res-concat\testData\1\res\layout\db_materialtype_edit.xml"<br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\menu\main.xml" and "[...]\ant-android-res-concat\testData\2\res\menu\main.xml"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values\dimens.xml"<br/>
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_horizontal_margin']"<br/>
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_vertical_margin']"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\strings.xml" and "[...]\ant-android-res-concat\testData\2\res\values\strings.xml"<br/>
[android.res.merge] Remove /resources/string[@name='db_shortname']<br/>
[android.res.merge] Remove /resources/string[@name='db_materialtype']<br/>
[android.res.merge] Remove /resources/string[@name='db_material_dictionary']<br/>
[android.res.merge] Found duplicated node - "/resources/string[@name='app_name']"<br/>
[android.res.merge] Found duplicated node - "/resources/string[@name='action_settings']"<br/>
[android.res.merge] Found duplicated node - "/resources/string[@name='hello_world']"<br/>
[android.res.merge] Found duplicated node - "/resources/string[@name='material_kind_dictionary']"<br/>
[android.res.merge] Adding /resources/string[@name='db_shortname']<br/>
[android.res.merge] Adding /resources/string[@name='db_materialtype']<br/>
[android.res.merge] Adding /resources/string[@name='db_material_dictionary']<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values\styles.xml"<br/>
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Light']"<br/>
[android.res.merge] Found duplicated node - "/resources/style[@name='AppTheme'][@parent='AppBaseTheme']"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-sw600dp\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values-sw600dp\dimens.xml"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-sw720dp-land\dimens.xml" and "[...]\ant-android-res-concat\testData\2\res\values-sw720dp-land\dimens.xml"<br/>
[android.res.merge] Found duplicated node - "/resources/dimen[@name='activity_horizontal_margin']"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-v11\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values-v11\styles.xml"<br/>
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light']"<br/>
[android.res.merge] <br/>
[android.res.merge] Merging "[...]\ant-android-res-concat\testData\1\res\values-v14\styles.xml" and "[...]\ant-android-res-concat\testData\2\res\values-v14\styles.xml"<br/>
[android.res.merge] Found duplicated node - "/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light.DarkActionBar']"<br/>
[android.res.merge] <br/>

BUILD SUCCESSFUL<br/>
Total time: 0 seconds<br/>

