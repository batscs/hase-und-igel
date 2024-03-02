package logic;

import com.google.gson.annotations.Expose;
import logic.data.Validation;

/**
 * Klasse, welche einen Spieler im Hase-Und-Igel-Spiel repräsentiert.
 *
 * @author github.com/batscs
 */
public class Player {

    /**
     * Attribut für den Spielernamen
     */
    @Expose
    private String name;

    @Expose
    private boolean suspended, eatsSalad;

    /**
     * Attribute für die aktuelle Position des Spielers.
     */
    @Expose
    private int position;

    /**
     * Attribute für die die Anzahl an Karotten des Spielers.
     */
    @Expose
    private int carrots;

    /**
     * Attribute für die Anzahl an Salate des Spielers.
     */
    @Expose
    private int salads;

    /**
     * Konstruktor zum Initialisieren eines Spielers mit einem vorgegebenen Spielernamen.
     *
     * @param username der Spielername
     */
    public Player(String username) {
        this.name = username;

        moveTo(0);
    }

    /**
     * Verändert die Position des Spielers auf die übergebene Position.
     *
     * @param position die übergebene Position
     */
    public void moveTo(int position) {
        this.position = position;
    }

    /**
     * Methode, welche anhand des Parameters entscheidet, ob der Spieler Salat isst.
     *
     * @param b True, falls Spieler Salat isst.
     */
    public void setEatsSalad(boolean b) {
        this.eatsSalad = b;
    }

    /**
     * Methode, welche den Spielernamen ermittelt.
     *
     * @return Der Spielername
     */
    public String getName() {
        return name;
    }

    /**
     * Methode, welche ermittelt ob der Spieler gerade aussetzt.
     *
     * @return True, falls Spieler gerade aussetzt.
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Methode, welche anhand des Parameters entscheidet, ob der Spieler aussetzt.
     *
     * @param b True, falls Spieler aussetzt
     */
    public void setSuspended(boolean b) {
        this.suspended = b;
    }

    /**
     * Methode, welche ermittelt ob der Spieler gerade Salat isst.
     *
     * @return True, falls der Spieler gerade Salat isst.
     */
    public boolean eatsSalad() {
        return eatsSalad;
    }

    /**
     * Methode, welche die aktuelle Position des Spielers ermittelt.
     *
     * @return Die Position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Methode, welche die Anzahl an Karotten des Spielers ermittelt.
     *
     * @return Die Anzahl an Karotten
     */
    public int getCarrots() {
        return carrots;
    }

    /**
     * Methode, welche die Anzahl an Karotten vom Spieler setzt.
     *
     * @param i Der neue Wert an Karotten.
     */
    public void setCarrots(int i) {
        this.carrots = i;
    }

    /**
     * Methode, welche die Anzahl an Salaten des Spielers ermittelt.
     *
     * @return Die Anzahl an Salaten.
     */
    public int getSalads() {
        return salads;
    }

    /**
     * Methode, welche die Anzahl an Salaten des Spielers setzt.
     *
     * @param i Der neue Wert an Salaten.
     */
    public void setSalads(int i) {
        this.salads = i;
    }

    /**
     * Methode zum Validieren, ob die Attribute des Spielers Regelkonform mit dem Spiel sind.
     * Wird genutzt beim Laden eines Spielstandes.
     *
     * @return Validation.VALID wenn alles in Ordnung, sonst entsprechender INVALID-Enum.
     */
    public Validation validate() {
        if (name == null || name.isEmpty()) {
            return Validation.INVALID_PLAYER_USERNAME;
        }

        if (position < 0 || position > GameLogic.FINAL_FIELD_POSITION) {
            return Validation.INVALID_PLAYER_POSITION;
        }

        if (carrots < 0) {
            return Validation.INVALID_PLAYER_CARROTS;
        }

        if (salads < 0) {
            return Validation.INVALID_PLAYER_SALADS;
        }

        if (position == GameLogic.FINAL_FIELD_POSITION) {
            if (salads > 0) {
                return Validation.INVALID_PLAYER_FINISH_TOO_MANY_SALADS;
            }

            if (carrots > 10) {
                return Validation.INVALID_PLAYER_FINISH_TOO_MANY_CARROTS;
            }
        }

        return Validation.VALID;
    }

    /**
     * Methode zum Kopieren eines Spielers. Liefert keine Referenz, sondern eine tiefe Kopie.
     *
     * @return tiefe Kopie des Spielers.
     */
    Player copy() {
        Player p = new Player(getName());
        p.setSuspended(suspended);
        p.setEatsSalad(eatsSalad);
        p.setCarrots(carrots);
        p.setSalads(salads);
        p.moveTo(position);

        return p;
    }

    @Override
    public String toString() {
        return String.format(
                "Player = {name: %s, position: %d, carrots: %d, salads: %d, suspended: %b, eatsSalad: %b}",
                name, position, carrots, salads, suspended, eatsSalad);
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;

        return p.getName().equals(getName())  && p.getSalads() == getSalads() && p.getCarrots() == getCarrots()
                && p.getPosition() == getPosition() && p.isSuspended() == isSuspended() && p.eatsSalad() == eatsSalad();
    }

}
