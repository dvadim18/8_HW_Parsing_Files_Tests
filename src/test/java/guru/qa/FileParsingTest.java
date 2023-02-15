package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTest {

    ClassLoader cl = FileParsingTest.class.getClassLoader();// для чтения файла из ресурсов

    @Test
    void zipParseCSVTest() throws Exception {
        try (InputStream resource = cl.getResourceAsStream("csv.zip");
             ZipInputStream zis = new ZipInputStream(resource)

        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                CSVReader reader = new CSVReader(new InputStreamReader(zis));
                List<String[]> content = reader.readAll();

                assertThat(content.get(0)[1]).contains("Jordan");

            }
        }
    }


    @Test
    void zipParsePDFTest() throws Exception {
        try (InputStream resource = cl.getResourceAsStream("pdf.zip");
             ZipInputStream zis = new ZipInputStream(resource)

        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                PDF content = new PDF(zis);
                assertThat(content.author).contains("Sam Brannen");
                System.out.println("1");

            }
        }
    }

    @Test
    void zipParseXLSTest() throws Exception {
        try (InputStream resource = cl.getResourceAsStream("sample-xlsx-file.zip");
             ZipInputStream zis = new ZipInputStream(resource)

        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                XLS content = new XLS(zis);
                assertThat(content.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");

            }
        }
    }


    @Test
    void jsonParseTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream resource = cl.getResourceAsStream("json.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            assertThat(jsonObject.get("title").getAsString()).isEqualTo("example glossary");
            assertThat(jsonObject.get("gloss_div").getAsJsonObject().get("title").getAsString()).isEqualTo("S");
            assertThat(jsonObject.get("gloss_div").getAsJsonObject().get("flag").getAsBoolean()).isTrue();
        }
    }

}
