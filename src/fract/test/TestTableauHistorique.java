package fract.test;
import fract.struct.*;

public class TestTableauHistorique {

	public static void main(String[] args) {
		HistoriqueTab ht = new HistoriqueTab();
		ht.ajouterVue(2, 3, 4);
		ht.ajouterVue(5, 6, 7);
		ht.ajouterVue(8, 9, 10);
		ht.ajouterVue(11, 12, 13);
		ht.afficher();
		ht.getDouble(2, 0);
		ht.ajouterVue(14, 15, 16);
		ht.afficher();
	}

}
