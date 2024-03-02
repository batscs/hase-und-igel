package logic.data;

/**
 * Enum zur Repräsentation der verschiedenen Zahlen- oder Flaggenkarten.
 * Speichern jeweils die anzustrebene Platzierung des daraufstehenden Spielers.
 *
 * @author github.com/batscs
 */
public enum NummerFlag {

    TWO(2),
    THREE(3),
    FOUR(4),
    FLAG(1,5,6);

    /**
     * Array mit den anzustrebenden Plätzen welche auf dem Feld gültig sind, um Karotten zu erhalten.
     * Da Flaggenkarten diverse Plätze erlauben ist es ein Array.
     */
    private int[] desiredPlace;

    /**
     * Konstruktor zum definieren der Plätze
     * @param places Parameter für die diversen Plätze
     */
    NummerFlag(int... places) {
        desiredPlace = places;
    }

    /**
     * Getter für den gewünschten Platz
     *
     * @return die anzustrebenden Plätze
     */
    public int[] getDesiredPlace() {
        return desiredPlace;
    }

}
