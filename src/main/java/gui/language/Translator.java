package gui.language;

import logic.Token;

/**
 * Schnittstelle für einen Translator, welcher Tokens aus dem Spiel in Klartext für die Oberfläche
 * übersetzen kann.
 *
 * @author github.com/batscs
 */
public interface Translator {

    /**
     * Methode zum direkten Übersetzen von einem Token in Klartext
     *
     * @param msg Der Token
     * @return Übersetzer Klartext
     */
    String translate(Token msg);

    /**
     * Methode zum Erneuern von Übersetzungsinformationen.
     */
    void refresh();

}
