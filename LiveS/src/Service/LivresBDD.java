package Service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LivresBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "eleves.db";

	private static final String TABLE_LIVRES = "table_livres";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_ISBN = "ISBN";
	private static final int NUM_COL_ISBN = 1;
	private static final String COL_TITRE = "Titre";
	private static final int NUM_COL_TITRE = 2;

	private SQLiteDatabase bdd;

	private MaBaseSQLite maBaseSQLite;

	public LivresBDD(Context context) {
		// On créer la BDD et sa table
		maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open() {
		// on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}

	public void close() {
		// on ferme l'accès à la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD() {
		return bdd;
	}

	public long insertLivre(Live livre) {
		// Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associé à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(COL_ISBN, livre.getIsbn());
		values.put(COL_TITRE, livre.getTitre());
		// on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_LIVRES, null, values);
	}

	public int updateLivre(int id, Live livre) {
		// La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme
		// une insertion
		// il faut simple préciser quelle livre on doit mettre à jour grâce à
		// l'ID
		ContentValues values = new ContentValues();
		values.put(COL_ISBN, livre.getIsbn());
		values.put(COL_TITRE, livre.getTitre());
		return bdd.update(TABLE_LIVRES, values, COL_ID + " = " + id, null);
	}

	public int removeLivreWithID(int id) {
		// Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(TABLE_LIVRES, COL_ID + " = " + id, null);
	}

	public List<Live> getLivreWithTitre() {
		// Récupère dans un Cursor les valeur correspondant à un livre contenu
		// dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_LIVRES, new String[] { COL_ID, COL_ISBN,
				COL_TITRE }, null, null, null, null, null);
		return cursorToLivre(c);
	}

	

	// Cette méthode permet de convertir un cursor en un livre
	private List<Live> cursorToLivre(Cursor c) {
		List<Live> liveList = new ArrayList<Live>();

		// si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;

		// Sinon on se place sur le premier élément
		c.moveToFirst();
		// On créé un livre
		// on lui affecte toutes les infos grâce aux infos contenues dans le
		// Cursor
		do {

			Live live = new Live();

			live.setId(c.getInt(NUM_COL_ID));
			live.setIsbn(c.getString(NUM_COL_ISBN));
			live.setTitre(c.getString(NUM_COL_TITRE));
			// On ferme le cursor
			liveList.add(live);
		} while (c.moveToNext());
		c.close();
		return liveList;

	}
}
