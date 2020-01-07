
import java.io.*;
import java.util.*;

public class ProblemeLineaire
{
	private int n; // Nombre de variables (sans les variables d'écart).
	private int m; // Nombre de contraintes (sans les contraintes de signe).
	private double[][] matricePL; // Matrice stockant le PL.
	private int[] indiceVariableEnBase; // Tableau contenant les variables en base.

	public ProblemeLineaire(String nomFichier) // Constructeur prenant en paramètre le nom du fichier.
	{
    	try
    	{
    		Scanner scanner = new Scanner(new FileReader(nomFichier)); // Initialisation du scanner.
    		scanner.useLocale(Locale.US); // Locale US pour lire des doubles avec le point pour séparer la partie décimale.

			this.n = scanner.nextInt(); // Lecture du nombre de variables.
			this.m = scanner.nextInt(); // Lecture du nombre de contraintes.
			this.matricePL = new double[this.m + 1][1 + this.n + this.m + 1]; // Création de la matrice.
			this.indiceVariableEnBase = new int[this.m]; // Création de la matrice.

			// Ajout des z (ou x0).
			for(int i = 0; i < this.m + 1; i++)
				this.matricePL[i][0] = 0;

			// Lecture et ajout des coefficients de la fonction objective.
 			for(int j = 1; j <= this.n; j++)
     			this.matricePL[0][j] = scanner.nextDouble();

 			// Lecture et ajout des coefficients des contraintes.
 			for(int i = 1; i <= this.m; i++)
 			{
 				int stockJ = 0;

 				for(int j = 1; j <= this.n; j++)
 				{
	     			this.matricePL[i][j] = scanner.nextDouble();
	     			stockJ = j; // Stockage de j pour afficher les coûts.
 				}

 				this.matricePL[i][stockJ + 1 + this.m] = scanner.nextDouble(); // Lecture de la valeur du membre de droite de la contrainte (ajout des coûts)
 			}

 			// Ajout de la matrice identitée.
 			for(int i = 1; i <= this.m; i++)
 				this.matricePL[i][this.n + i] = 1;

 			scanner.close(); // Fermeture du scanner.
 		}

    	catch(FileNotFoundException ex)
 		{
 			System.out.println("Erreur ! Le fichier n'a pas été trouvé.");
 		}
	}

 	public int retournerN() // Accesseur pour n.
	{
		return this.n;
 	}

	public int retournerM() // Accesseur pour m.
 	{
		return this.m;
 	}

	public double[][] retournerMatricePL() // Accesseur pour la matrice.
 	{
		return this.matricePL;
 	}

	public int[] retournerVariableEnBase() // Accesseur pour le tableau des variables en base.
 	{
		return this.indiceVariableEnBase;
 	}

	public String toString() // Méthode permettant l'affichage de la matrice.
	{
		 String s = "";

		 for(int i = 0; i < this.m + 1; i++)
		 {
			 for(int j = 0; j < 1 + this.n + this.m + 1 ; j++)
				 //s = s + "(" + i + ", " + j + ")" + " | ";
				 s = s + String.format("%1$10.1f", this.retournerMatricePL()[i][j]);
			 s = s + "\n";
		 }

		 return s;
	 }

	 public int varEntrante() // Méthode retournant l'indice d'une variable entrante ou -1.
	 {
		int indiceVarEntrante = -1;

		for(int j = 1; j <= this.n + this.m; j++)
		{
			if(this.retournerMatricePL()[0][j] > 0)
				indiceVarEntrante = j;
		}

		//System.out.println("Indice j variable entrante : " + indiceVarEntrante);

		return indiceVarEntrante;
	 }

	 public int varSortante(int indiceVarEntrante) // Méthode retournant l'indice d'une variable sortante ou -1.
	 {
		int indiceVarSortante = -1 ;
		double[] calcul = new double[this.m + 1];
		double val = Double.MAX_VALUE;

		if(indiceVarEntrante != -1) // S'il y a une variable entrante ...
		{
			for(int i = 1; i < this.m + 1; i++) // Stockage de chaque rapport dans un tableau avec les m�mes indices.
				if(this.retournerMatricePL()[i][indiceVarEntrante] > 0)
					calcul[i] = this.retournerMatricePL()[i][this.n + this.m + 1] / this.retournerMatricePL()[i][indiceVarEntrante];

			//System.out.println(Arrays.toString(tabCalcul));

			for(int ind = 1; ind < calcul.length ; ind++) // Comparaison des différents résultats pour trouver l'indice i du pivot.
			{
				if(calcul[ind] < val && calcul[ind] != 0)
				{
					val = calcul[ind];
					indiceVarSortante = ind;
				}
			}
		}

		//System.out.println("Indice i variable sortante : " + indiceVarSortante);
		return indiceVarSortante;
	 }

	 public void pivot(int indiceVarEntrante, int indiceVarSortante) // Méthode permettant de mettre à jour la matrice.
	 {
		 double stock;

		 if(indiceVarSortante!= -1) // S'il y a une variable entrante ...
		 {
			double pivot = this.retournerMatricePL()[indiceVarSortante][indiceVarEntrante];
			System.out.println("Pivot : " + pivot); // Affiche le pivot.

			for(int i = 0; i < this.m + 1; i++) // Parcourt la matrice.
			{
				stock = this.retournerMatricePL()[i][indiceVarEntrante];

				 for(int j = 1; j < 1 + this.n + this.m + 1 ; j++)
				 {
					if(i == indiceVarSortante) // Traitement spécial de la ligne du pivot.
						this.retournerMatricePL()[i][j] = this.retournerMatricePL()[i][j] / pivot; // Division de la ligne du pivot par le pivot.

					else
					{
						System.out.print("Case [" + i +"][" + j + "]" + " : " + this.retournerMatricePL()[i][j] + " - ((" + stock + " / " + pivot + ") * " + this.retournerMatricePL()[indiceVarSortante][j] + "))" );
						this.retournerMatricePL()[i][j] = this.retournerMatricePL()[i][j] - ((stock / pivot) * this.retournerMatricePL()[indiceVarSortante][j]);
						System.out.println(" = " + this.retournerMatricePL()[i][j]);
					}
				 }
			}
		 }
	 }

	 public boolean solve() // Méthode résolvant le PL.
	 {
		 int cpt = 1;

		 while(this.varEntrante() != -1) // Tant qu'il y a une variable entrante ...
		 {
			 System.out.println("\n/// ITERATION " + cpt + " ///\n");
			 System.out.println("Matrice :\n" + this);
			 this.indiceVariableEnBase[cpt] = this.varSortante(this.varEntrante());
			 pivot(this.varEntrante(), this.indiceVariableEnBase[cpt]);

			 if(cpt > 100)
				 return false;
			 cpt++;
		 }

		 return true;
	 }

	 public void afficheSolution() // Méthode permettant d'afficher le coût et les solutions du PL.
	 {
		System.out.println("Coût : " + Math.abs(this.retournerMatricePL()[0][1 + this.n + this.m])); // Affichage du coût.
	 }
}
