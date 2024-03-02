package gui.data;

/**
 * Enum, welches genaue Spezifikationen zu dem Programm liefert. Hier wird nicht mit einem Enum
 * gearbeitet, da die Spezifikationen universell gelten.
 *
 * @author github.com/batscs
 */
public enum Specification {

    VERSION_MAJOR("1"),
    VERSION_MINOR("0"),
    VERSION_PATCH("0"), APPLICATION_VERSION(
            String.join(".", VERSION_MAJOR.toString(), VERSION_MINOR.toString(),
                    VERSION_PATCH.toString())),
    APPLICATION_AUTHOR("github.com/batscs"), APPLICATION_VERSION_PUBLISHED("04.12.2023"),
    APPLICATION_TITLE("Hase und Igel");

    /**
     * Attribut, die Spezifikation als Klartext enthält.
     */
    private final String value;

    /**
     * Konstruktor zum Initialisieren des Enums mit dem jeweiligen Wert als Klartext.
     */
    Specification(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
