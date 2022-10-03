public class Main {
    public static void main(String[] args) {
        String[] s = {"Jens","Per","Kari","Ole","Berit","Jens","Anne","Nils","Siv"};

        hjelpeklasser.Liste<String> liste = new hjelpeklasser.Liste.TabellListe<>();

        for (String navn : s) liste.leggInn(0,navn);  // legger inn først

        System.out.println("Vi henter " + liste.hent(5) + ".");

        System.out.println("Nils er på plass " + liste.indeksTil("Nils") + "!");

        liste.oppdater(2,"Anna");  // bytter ut Anne med Anna på plass 2

        System.out.println(liste.fjern(0) + " er slettet!");

        System.out.println("Listeinnhold: " + liste);

        liste.fjernHvis(x -> x.equals("Jens"));  // fjerner alle forekomster av Jens

        liste.forEach(x -> System.out.print(x + " "));



    }
}
