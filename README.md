ant-android-res-concat
======================

���� ��� Apache Ant ��� ���������� ������� �������� (����� "res") ���� android-��������.

������������ (�� ������� ������) ��� ������� � ���� ���� ���������� ��������������� ������ ������ android-�������.
������� ���������� ���������� "������������ ����" �������� �� ���������, ��� ������� ��������������� �������� ������������ ������������ ����������� ���� ����� ��������������.
����� ������� ����� ������ (����� ��) "New build system" (���������� �� Gradle) - �� ������������������ � ������� 0.3 - ���� �� �������.

������ ������������� - � "test/build.xml"

���� ������������ ��������� ���������:
to - �������� ���� �� �������� �������, � ������� ����� ���������� �������
prefixto - ������� ����, �������������� � ������������� �������
from - �������� ���� �� �������� �������, ������� ����� ��������� � ��������, ��������� � "to"
prefixfrom - ������� ����, �������������� �� "from" �������.

�������� ������ �����:
1. ���� �� "from" ��������� xml-����, ������� ����������� � "to" - �� �� ���������� � "to"
2. ���� ���� �� "from" ������������ � "to" - �� ���������� ��������� �� ����������� �� ��������� ��������:
2.1 �� ��������� "to" ��������������� ��������� ��� ��������, ������� ������� "name", ������������ � prefixfrom
2.2 ��������������� �������������� �������� ��������� �� "from" (��� �������� ����� ���������� ������� ��������� �� "from" - item ,� �������� �� "to" - toDoc):
2.2.1. ���� item �� ����� �������� name:
2.2.1.1 ����������� ������� � toDoc ����� �� ������� (�� nodeName)
2.2.1.2 ���� ����� ������� ��� - ��� ����������� � toDoc. ���� ���� - �� ������������ (������ ������������ �� child-���������)
2.2.2. ���� item ����� ������� name: 
2.2.2.1 ���� name ���������� � prefixto - ��� ������������ (��� �����������, ���, � �������, �������� � strings.xml ������� "to" �� ����� ������� ���������������)
2.2.2.2 ���� name ���������� � prefixfrom - ��� ����������� � toDoc
2.2.2.3 ���� name �� ���������� �� � prefixto, �� � prefixfrom - �� ����������� ������� ����� ������� � ����� name � toDoc. ���� ����� ������� ��� - ��� �����������. ���� ���� - ������������

������ ����������� ������ �����:
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

