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
            "Merging manifest \"%1_man\\AndroidManifest.xml\" and \"%2_man\\AndroidManifest.xml\"",
            "",
            "Copying \"%2_res\\layout\\db_activity_main.xml\" to \"%1_res\\layout\\db_activity_main.xml\"",
            "Copying \"%2_res\\layout\\db_materialdic_listitem.xml\" to \"%1_res\\layout\\db_materialdic_listitem.xml\"",
            "Copying \"%2_res\\layout\\db_materialdic_main.xml\" to \"%1_res\\layout\\db_materialdic_main.xml\"",
            "Copying \"%2_res\\layout\\db_materialtype_edit.xml\" to \"%1_res\\layout\\db_materialtype_edit.xml\"",
            "Merging \"%1_res\\menu\\main.xml\" and \"%2_res\\menu\\main.xml\"",
            "",
            "Merging \"%1_res\\values\\dimens.xml\" and \"%2_res\\values\\dimens.xml\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_horizontal_margin']\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_vertical_margin']\"",
            "",
            "Merging \"%1_res\\values\\strings.xml\" and \"%2_res\\values\\strings.xml\"",
            "Remove /resources/string[@name='db_shortname']",
            "Remove /resources/string[@name='db_materialtype']",
            "Remove /resources/string[@name='db_material_dictionary']",
            "Remove /resources/string-array[@name='db_length_array']",
            "Found duplicated node - \"/resources/string[@name='app_name']\"",
            "Found duplicated node - \"/resources/string[@name='action_settings']\"",
            "Found duplicated node - \"/resources/string[@name='hello_world']\"",
            "Found duplicated node - \"/resources/string[@name='material_kind_dictionary']\"",
            "Adding /resources/string[@name='db_shortname']",
            "Adding /resources/string[@name='db_materialtype']",
            "Adding /resources/string[@name='db_material_dictionary']",
            "Adding /resources/string-array[@name='db_length_array']",
            "",
            "Merging \"%1_res\\values\\styles.xml\" and \"%2_res\\values\\styles.xml\"",
            "Found duplicated node - \"/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Light']\"",
            "Found duplicated node - \"/resources/style[@name='AppTheme'][@parent='AppBaseTheme']\"",
            "",
            "Merging \"%1_res\\values-ru\\strings.xml\" and \"%2_res\\values-ru\\strings.xml\"",
            "Found duplicated node - \"/resources/string[@name='app_name']\"",
            "Found duplicated node - \"/resources/string[@name='sectionCaption']\"",
            "Found duplicated node - \"/resources/string[@name='btnNextCaption']\"",
            "Found duplicated node - \"/resources/string[@name='btnExitCaption']\"",
            "Found duplicated node - \"/resources/string[@name='channelCaption']\"",
            "",
            "Merging \"%1_res\\values-sw600dp\\dimens.xml\" and \"%2_res\\values-sw600dp\\dimens.xml\"",
            "",
            "Merging \"%1_res\\values-sw720dp-land\\dimens.xml\" and \"%2_res\\values-sw720dp-land\\dimens.xml\"",
            "Found duplicated node - \"/resources/dimen[@name='activity_horizontal_margin']\"",
            "",
            "Merging \"%1_res\\values-v11\\styles.xml\" and \"%2_res\\values-v11\\styles.xml\"",
            "Found duplicated node - \"/resources/style[@name='AppBaseTheme'][@parent='android:Theme.Holo.Light']\"",
            "",
            "Merging \"%1_res\\values-v14\\styles.xml\" and \"%2_res\\values-v14\\styles.xml\"",
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
        String processed = sout
                .toString()
                .replace("%1_res", new File("testData/1/res").getAbsolutePath())
                .replace("%2_res", new File("testData/2/res").getAbsolutePath())
                .replace("%1_man", new File("testData/1/").getAbsolutePath());
        processed = processed.replace("%2_man",
                new File("testData/2/").getAbsolutePath());
        assertEquals("Procesing result check", processed, bout.toString());
    }
}
