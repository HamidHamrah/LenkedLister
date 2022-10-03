import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class hjelpeklasser {
    public interface Beholder<T> extends Iterable<T>  // ny versjon
    {
        public boolean leggInn(T verdi);    // legger inn verdi i beholderen
        public boolean inneholder(T verdi); // sjekker om den inneholder verdi
        public boolean fjern(T verdi);      // fjerner verdi fra beholderen
        public int antall();                // returnerer antallet i beholderen
        public boolean tom();               // sjekker om beholderen er tom
        public void nullstill();            // tømmer beholderen
        public Iterator<T> iterator();      // returnerer en iterator

        default boolean fjernHvis(Predicate<? super T> p)  // betingelsesfjerning
        {
            Predicate<? super T> predicate = Objects.requireNonNull(p);// kaster unntak

            boolean fjernet = false;
            for (Iterator<T> i = iterator(); i.hasNext(); )  // løkke
            {
                if (p.test(i.next()))                          // betingelsen
                {
                    i.remove(); fjernet = true;                  // fjerner
                }
            }
            return fjernet;
        }
    } // grensesnitt Beholder
    public interface Liste<T> extends Beholder<T>
    {
        public boolean leggInn(T verdi);           // Nytt element bakerst
        public void leggInn(int indeks, T verdi);  // Nytt element på plass indeks
        public boolean inneholder(T verdi);        // Er verdi i listen?
        public T hent(int indeks);                 // Hent element på plass indeks
        public int indeksTil(T verdi);             // Hvor ligger verdi?
        public T oppdater(int indeks, T verdi);    // Oppdater på plass indeks
        public boolean fjern(T verdi);             // Fjern objektet verdi
        public T fjern(int indeks);                // Fjern elementet på plass indeks
        public int antall();                       // Antallet i listen
        public boolean tom();                      // Er listen tom?
        public void nullstill();                   // Listen nullstilles (og tømmes)
        public Iterator<T> iterator();             // En iterator

        public default String melding(int indeks)  // Unntaksmelding
        {
            return "Indeks: " + indeks + ", Antall: " + antall();
        }

        public default void indeksKontroll(int indeks, boolean leggInn)
        {
            if (indeks < 0 ? true : (leggInn ? indeks > antall() : indeks >= antall()))
                throw new IndexOutOfBoundsException(melding(indeks));
        }

        public class TabellListe<T> implements Liste<T>
        {
            private T[] a;
            private int antall;

            @SuppressWarnings("unchecked")          // pga. konverteringen: Object[] -> T[]
            public TabellListe(int størrelse)       // konstruktør
            {
                a = (T[])new Object[størrelse];       // oppretter tabellen
                antall = 0;                           // foreløpig ingen verdier
            }

            public TabellListe()                    // standardkonstruktør
            {
                this(10);                             // startstørrelse på 10
            }

            public TabellListe(T[] a)               // en T-tabell som parameter
            {
                this.a = a.clone();                   // kloner parametertabellen
                antall = a.length;                    // alle i tabellen
            }

            @Override
            public boolean leggInn(T verdi)  // inn bakerst
            {
                Objects.requireNonNull(verdi, "null er ulovlig!");

                // En full tabell utvides med 50%
                if (antall == a.length)
                {
                    a = Arrays.copyOf(a,(3*antall)/2 + 1);
                }

                a[antall++] = verdi;   // setter inn ny verdi

                return true;
            }

            @Override
            public void leggInn(int indeks, T verdi)
            {
                Objects.requireNonNull(verdi, "null er ulovlig!");

                indeksKontroll(indeks, true);  // true: indeks = antall er lovlig

                // En full tabell utvides med 50%
                if (antall == a.length) a = Arrays.copyOf(a,(3*antall)/2 + 1);

                // rydder plass til den nye verdien
                System.arraycopy(a, indeks, a, indeks + 1, antall - indeks);

                a[indeks] = verdi;     // setter inn ny verdi

                antall++;
            }

            @Override
            public int antall()
            {
                return antall;          // returnerer antallet
            }

            @Override
            public boolean tom()
            {
                return antall == 0;     // listen er tom hvis antall er 0
            }

            @Override
            public T hent(int indeks)
            {
                indeksKontroll(indeks, false);   // false: indeks = antall er ulovlig
                return a[indeks];                // returnerer er tabellelement
            }

            @Override
            public int indeksTil(T verdi)
            {
                for (int i = 0; i < antall; i++)
                {
                    if (a[i].equals(verdi)) return i;   // funnet!
                }
                return -1;   // ikke funnet!
            }

            @Override
            public boolean inneholder(T verdi)
            {
                return indeksTil(verdi) != -1;
            }

            @Override
            public T oppdater(int indeks, T verdi)
            {
                Objects.requireNonNull(verdi, "null er ulovlig!");

                indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig

                T gammelverdi = a[indeks];      // tar vare på den gamle verdien
                a[indeks] = verdi;              // oppdaterer
                return gammelverdi;             // returnerer den gamle verdien
            }

            @Override
            public T fjern(int indeks)
            {
                indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig
                T verdi = a[indeks];

                antall--; // sletter ved å flytte verdier mot venstre
                System.arraycopy(a, indeks + 1, a, indeks, antall - indeks);
                a[antall] = null;   // tilrettelegger for "søppeltømming"

                return verdi;
            }

            @Override
            public boolean fjern(T verdi)
            {
                Objects.requireNonNull(verdi, "null er ulovlig!");

                for (int i = 0; i < antall; i++)
                {
                    if (a[i].equals(verdi))
                    {
                        antall--;
                        System.arraycopy(a, i + 1, a, i, antall - i);

                        a[antall] = null;

                        return true;
                    }
                }
                return false;
            }

            @Override
            public void nullstill()
            {
                if (a.length > 10)
                    a = (T[])new Object[10];
                else
                    for (int i = 0; i < antall; i++)
                    {
                        a[i] = null;
                    }

                antall = 0;
            }

            @Override
            public String toString()
            {
                if (antall == 0) return "[]";

                StringBuilder s = new StringBuilder();
                s.append('[').append(a[0]);

                for (int i = 1; i < antall; i++)
                {
                    s.append(',').append(' ').append(a[i]);
                }
                s.append(']');

                return s.toString();
            }

            @Override
            public Iterator<T> iterator()
            {
                return new TabellListeIterator();
            }

            // Skal ligge som en indre klasse i class TabellListe
            private class TabellListeIterator implements Iterator<T>
            {
                private int denne = 0;             // instansvariabel
                private boolean removeOK = false;  // instansvariabel

                @Override
                public boolean hasNext()     // sjekker om det er flere igjen
                {
                    return denne < antall;     // sjekker verdien til denne
                }

                @Override
                public T next()
                {
                    if (!hasNext())
                        throw new NoSuchElementException("Tomt eller ingen verdier igjen!");

                    T denneVerdi = a[denne];   // henter aktuell verdi
                    denne++;                   // flytter indeksen
                    removeOK = true;           // nå kan remove() kalles

                    return denneVerdi;         // returnerer verdien
                }

                @Override
                public void remove()
                {
                    if (!removeOK) throw new IllegalStateException("Ulovlig tilstand!");

                    removeOK = false;          // remove() kan ikke kalles på nytt

                    // verdien i denne - 1 skal fjernes da den ble returnert i siste kall
                    // på next(), verdiene fra og med denne flyttes derfor en mot venstre

                    antall--;           // en verdi vil bli fjernet
                    denne--;            // denne må flyttes til venstre

                    System.arraycopy(a, denne + 1, a, denne, antall - denne);  // tetter igjen
                    a[antall] = null;   // verdien som lå lengst til høyre nulles
                }

            } // TabellListeIterator

        } // TabellListe


    }



}
