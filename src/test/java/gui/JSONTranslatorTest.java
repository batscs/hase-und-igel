package gui;

import gui.data.GUIValidation;
import gui.language.JSONTranslator;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class JSONTranslatorTest {

    public static final String PATH_DIRECTORY =
            String.join(File.separator, "src", "test", "files", "gui", "translator");

    private String toPath(String file) {
        return PATH_DIRECTORY + File.separator + file;
    }


    @Test
    public void validate_valid_Language() {
        String test = toPath("en_US.json");
        assertEquals(GUIValidation.VALID, JSONTranslator.validate(test));
    }

    @Test
    public void validate_invalid_jsonKeys_duplicate() {
        String test = toPath("language_broken_duplicate_keys.json");
        assertEquals(GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_GSON, JSONTranslator.validate(test));
    }

    @Test
    public void validate_invalid_jsonFormat() {
        String test = toPath("language_invalid_format.json");
        assertEquals(GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_GSON, JSONTranslator.validate(test));
    }

    @Test
    public void validate_invalid_incomplete() {
        String test = toPath("language_incomplete.json");
        assertEquals(GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_SIZE, JSONTranslator.validate(test));
    }

}
