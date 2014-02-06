package ru.lsv.ant.androidresconcat;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;

import org.junit.Before;
import org.junit.Test;

public class AndroidResConcatMergeManifestTest {

    /**
     * Тестовый таск
     */
    private AndroidResConcat concat;
    /**
     * Перехват out
     */
    private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
    /**
     * Результат, который должен получиться
     */
    String waitedString = "";

    @Before
    public void setUp() throws Exception {
        concat = new AndroidResConcat();
        //System.setOut(new PrintStream(bout));
    }

    @Test
    public final void testExecute() throws IOException {
        File sourceFile = new File("testData/3/AndroidManifest.xml");
        File destFile = new File("testData/5/AndroidManifest.xml");
        if (destFile.exists()) {
            destFile.delete();
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        concat.setto("testData/5/");
        concat.setfrom("testData/4/");
        concat.setprefixto("db_");
        concat.setprefixfrom("ui_");
        concat.execute();        
        assertEquals("Processing results...", waitedString, bout.toString());
    }
}
