package logic.data;

import util.ArrayUtils;

import java.util.Stack;

/**
 * Klasse zur Repräsentation eines Kartendecks.
 *
 * @author github.com/batscs
 */
public class CardDeck {

    /**
     * Attribut für das Kartendeck.
     */
    private final CardEvent[] cardDeck;

    /**
     * Attribut für die künstlichen (geschummelten) Karten welche als nächsten gezogen werden,
     * statt dem eigentlich Kartendeck.
     */
    private final Stack<CardEvent> cardFakeDeck;

    /**
     * Default-Konstruktor für das Kartendeck.
     */
    public CardDeck() {
        cardFakeDeck = new Stack<>();

        cardDeck = new CardEvent[] { CardEvent.REFUND, CardEvent.REFUND, CardEvent.TAKEORGIVE, CardEvent.TAKEORGIVE,
                CardEvent.SUSPEND, CardEvent.BACKWARDS, CardEvent.BACKWARDS, CardEvent.EATSALAD, CardEvent.FORWARDS,
                CardEvent.MOVEAGAIN, CardEvent.NEXTCARROT, CardEvent.PREVIOUSCARROT };

        ArrayUtils.shuffleArray(cardDeck);
    }

    /**
     * Methode zum Erhalten der aktuellsten / obersten Karte im Kartendeck. Diese Karte wird anschließend
     * an die letzte / hinterste Position des Decks verschoben. Falls das fakeDeck nicht leer ist, werden zuerst
     * Karten aus diesem gezogen, jedoch NICHT nach hinten rotiert.
     *
     * @return die gezogene Karte.
     */
    public CardEvent pop() {

        CardEvent card;

        if (cardFakeDeck.isEmpty()) {
            card = cardDeck[0];
            ArrayUtils.shiftArray(cardDeck);
        } else {
            card = cardFakeDeck.pop();
        }

        return card;
    }

    /**
     * Methode zum Manipulieren der pop()-Methode, indem zwangsweise eine bestimmte Karte einmal gezogen wird.
     * Die eingesetzten Karten werden NICHT nach hinten auf dem Kartenstapel rotiert, sondern gehen verloren.
     *
     * @param card Die eingesetzte Karte.
     */
    public void forcePush(CardEvent card) {
        cardFakeDeck.push(card);
    }

}
