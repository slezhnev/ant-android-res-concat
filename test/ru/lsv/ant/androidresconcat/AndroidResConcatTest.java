package ru.lsv.ant.androidresconcat;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class AndroidResConcatTest {

    /**
     * Тестовый таск
     */
    private AndroidResConcat concat;

    /**
     * Результат работы таска над тестовыми данными
     */
    private final String[] executeResult = {
            "Copying \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\2\\res\\layout\\db_activity_main.xml\" to \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\1\\res\\layout\\db_activity_main.xml\"",
            "Copying \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\2\\res\\layout\\db_materialdic_listitem.xml\" to \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\1\\res\\layout\\db_materialdic_listitem.xml\"",
            "Copying \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\2\\res\\layout\\db_materialdic_main.xml\" to \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\1\\res\\layout\\db_materialdic_main.xml\"",
            "Copying \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\2\\res\\layout\\db_materialtype_edit.xml\" to \"F:\\Lsv\\JavaProjects\\ant-android-res-concat\\testData\\1\\res\\layout\\db_materialtype_edit.xml\"",
            "Merging \"%1\\menu\\main.xml\" and \"%2\\menu\\main.xml\"",
            "",
            "Merging \"%1\\values\\dimens.xml\" and \"%2\\values\\dimens.xml\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_horizontal_margin']\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_vertical_margin']\"",
            "",
            "Merging \"%1\\values\\strings.xml\" and \"%2\\values\\strings.xml\"",
            "Remove /resources/string[@name='db_shortname']",
            "Remove /resources/string[@name='db_materialtype']",
            "Remove /resources/string[@name='db_material_dictionary']",
            "Found duplicated node - \"/resources/string[@name='app_name']\"",
            "Found duplicated node - \"/resources/string[@name='action_settings']\"",
            "Found duplicated node - \"/resources/string[@name='hello_world']\"",
            "Found duplicated node - \"/resources/string[@name='material_kind_dictionary']\"",
            "Adding /resources/string[@name='db_shortname']",
            "Adding /resources/string[@name='db_materialtype']",
            "Adding /resources/string[@name='db_material_dictionary']",
            "",
            "Merging \"%1\\values\\styles.xml\" and \"%2\\values\\styles.xml\"",
            "Found duplicated node - \"/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Light']\"",
            "Found duplicated node - \"/resources/style[@name='AppTheme'][@parent='AppBaseTheme']\"",
            "",
            "Merging \"%1\\values-sw600dp\\dimens.xml\" and \"%2\\values-sw600dp\\dimens.xml\"",
            "",
            "Merging \"%1\\values-sw720dp-land\\dimens.xml\" and \"%2\\values-sw720dp-land\\dimens.xml\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_horizontal_margin']\"",
            "",
            "Merging \"%1\\values-v11\\styles.xml\" and \"%2\\values-v11\\styles.xml\"",
            "Found duplicated node - \"/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light']\"",
            "",
            "Merging \"%1\\values-v14\\styles.xml\" and \"%2\\values-v14\\styles.xml\"",
            "Found duplicated node - \"/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light.DarkActionBar']\"",
            ""};

    /**
     * Перехват out
     */
    private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
    /**
     * Результат обработки из executeResult (чтобы соблюсти возможные
     * platform-specific проблемы с println
     */
    private final StringWriter sout = new StringWriter();

    @Before
    public final void setup() {
        concat = new AndroidResConcat();
        System.setOut(new PrintStream(bout));
    }

    @Test
    public final void testExecute() {
        concat.setto("testData/1/");
        concat.setfrom("testData/2/");
        concat.setprefixto("ui_");
        concat.setprefixfrom("db_");
        concat.execute();
        PrintWriter soutP = new PrintWriter(sout);
        for (String tmp : executeResult) {
            soutP.println(tmp);
        }
        String processed = sout.toString()
                .replace("%1", new File("testData/1/res").getAbsolutePath())
                .replace("%2", new File("testData/2/res").getAbsolutePath());
        assertEquals("Procesing result check", processed, bout.toString());
    }
}
