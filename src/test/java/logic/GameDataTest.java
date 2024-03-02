package logic;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GameDataTest {

    public static final String PATH_DIRECTORY =
            String.join(File.separator, "src", "test", "files", "logic", "gamedata");

    private String toPath(String file) {
        return PATH_DIRECTORY + File.separator + file;
    }

    @Test
    public void test_valid() {
        File testFile = new File(toPath("test_valid_standingOnCarrotField.json"));
        assertNotNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_currPlayer_negative() {
        File testFile = new File(toPath("test_invalid_currPlayer_negative.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_currPlayer_outOfBounds() {
        File testFile = new File(toPath("test_invalid_currPlayer_outOfBounds.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_duplicate() {
        File testFile = new File(toPath("test_invalid_onTarget_duplicate.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_negative() {
        File testFile = new File(toPath("test_invalid_onTarget_negative.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_notSet() {
        File testFile = new File(toPath("test_invalid_onTarget_notSet.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_outOfBounds() {
        File testFile = new File(toPath("test_invalid_onTarget_outOfBounds.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_outOfBounds2() {
        File testFile = new File(toPath("test_invalid_onTarget_outOfBounds2.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_onTarget_tooMany() {
        File testFile = new File(toPath("test_invalid_onTarget_tooMany.json"));
        assertNull(GameData.validate(testFile));
    }

    @Test
    public void test_invalid_players_position_duplicate() {
        File testFile = new File(toPath("test_invalid_players_position_duplicate.json"));
        assertNull(GameData.validate(testFile));
    }
}
