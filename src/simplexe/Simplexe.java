
import java.io.*;
import java.util.*;

public class Simplexe
{
	public static void main(String[] args)
	{
		ProblemeLineaire pl = new ProblemeLineaire("input3.txt");

		System.out.println("Nombre de variables : " + pl.retournerN() + "\nNombre de contraintes : " + pl.retournerM() + "\n");
		boolean optimal = pl.solve();
		System.out.println("\n/// FIN ///\n");
		System.out.println("Matrice finale :\n" + pl);

		if(!optimal)
			System.out.println("Ce problème linéaire n'a pas de solution optimale : il est non borné");
		else
			pl.afficheSolution();
    }
}
