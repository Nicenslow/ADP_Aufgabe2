package tuerme_von_Hanoi;

/**
 * Ein Paar ist einer Kontainer für zwei Elemente.
 */
public class Paar<T> {
    /**
     * Erstes Element im Kontainer.
     */
    private T erstes;

    /**
     * Zweites Element im Kontainer.
     */
    private T zweites;

    public Paar(T erstes, T zweites) {
        this.erstes = erstes;
        this.zweites = zweites;
    }

    public T getErstes() {
        return erstes;
    }

    public T getZweites() {
        return zweites;
    }

    @Override
    public String toString(){
        return erstes + "->" + zweites;
    }
}
