package com.rogger.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "OpenHelperBy.db";
	private static final int DB_VERSON = 1;
	public static OpenHelper INSTANCIA;
	private static String TB_NAME = "minhas_compras";
	private static String TYPE = "type";
	private static String COLUMN_ITEM = "product_name";
	private static String COLUMN_QUANT = "quantidade";
	private static String COLUMN_VALOR = "valor";
	private static String COLUMN_BARCOD = "barcod";
	private static String COLUMN_DTCOMPRA = "data";
	private static String COLUMN_IMAGE = "photo_data";

	public static OpenHelper getInstance(Context context) {
		if (INSTANCIA == null) {
			INSTANCIA = new OpenHelper(context);
		}
		return INSTANCIA;
	}

	public OpenHelper(@Nullable Context context) {
		super(context, DB_NAME, null, DB_VERSON);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TB_NAME + " (id INTEGER primary key," + COLUMN_ITEM + " TEXT, barcod TEXT,"
				+ " data DATETIME," + "photo_data TEXT," + "valor REAL," + "quantidade INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("TesteDate", "on Upgrade disparado");
		/*	if (oldVersion < newVersion) {
		String sql = "ALTER TABLE " + TB_NAME + " ADD COLUMN " + COLUMN_NOTE + " ";
		*/ //db.execSQL(sql);
	}
	//Buscando todos os dados .......
	//@SuppressLint("Range")
	public List<Registro> getRegisterBy() {
		List<Registro> register = new ArrayList<>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME + " ORDER BY id DESC", new String[] {});
		try {
			if (cursor.moveToFirst()) {
				do {
					Registro registro = new Registro();
					registro.id = cursor.getInt(cursor.getColumnIndex("id"));
					registro.name = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
					registro.preco = cursor.getFloat(cursor.getColumnIndex(COLUMN_VALOR));
					registro.barcode = cursor.getString(cursor.getColumnIndex(COLUMN_BARCOD));
					registro.quantidade = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANT));
					registro.data = cursor.getString(cursor.getColumnIndex(COLUMN_DTCOMPRA));
					registro.uri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
					register.add(registro);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("SQLite", e.getMessage(), e);
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}

		return register;
	}

	public List<Registro> getComprasPorMesEAno(int ano, int mes) {
		List<Registro> compras = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();

		// Formato para filtrar o mês e ano, por exemplo: "2023-11"
		String mesAno = String.format("%d-%02d", ano, mes);

		String query = "SELECT * FROM " + TB_NAME + " WHERE strftime('%Y-%m', " + COLUMN_DTCOMPRA + ") = ?";
		Cursor cursor = db.rawQuery(query, new String[] { mesAno });

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
				String item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM));
				float valor = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_VALOR));
				int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANT));
				String uri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
				String barcode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARCOD));
				String dataCompra = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DTCOMPRA));

				compras.add(new Registro(id, item, valor, quant, uri, barcode, dataCompra));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return compras;
	}

	//BUSCAR um item pelo Id
	public Registro buscarId(int id) {
		Registro registro = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME + " WHERE id = ?", new String[] { String.valueOf(id) });
		try {
			if (cursor.moveToFirst()) {
				do {
					registro = new Registro();
					registro.id = cursor.getInt(cursor.getColumnIndex("id"));
					registro.name = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
					registro.preco = cursor.getFloat(cursor.getColumnIndex(COLUMN_VALOR));
					registro.barcode = cursor.getString(cursor.getColumnIndex(COLUMN_BARCOD));
					registro.quantidade = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANT));
					registro.data = cursor.getString(cursor.getColumnIndex(COLUMN_DTCOMPRA));
					registro.uri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("SQLite", e.getMessage(), e);
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return registro;
	}
	//Busca por String
	public List<Registro> searchByString(String searchString) {
		List<Registro> register = new ArrayList<>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME + " WHERE " + COLUMN_ITEM + " LIKE ?",
				new String[] { "%" + searchString + "%" });
		try {
			if (cursor.moveToNext()) {
				do {
					Registro registro = new Registro();
					registro.id = cursor.getInt(cursor.getColumnIndex("id"));
					registro.data = cursor.getString(cursor.getColumnIndex("data"));
					registro.uri = cursor.getString(cursor.getColumnIndex("photo_data"));
					register.add(registro);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("SQLite", e.getMessage(), e);
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return register;
	}

	// ADICIONANDO DADOS.......
	public long additem(String name, float valor, String barcode, int amount, String uri) {
		LocalDate localDate = LocalDate.now();
		SQLiteDatabase dd = getWritableDatabase();
		long calcId = 0;
		try {
			dd.beginTransaction();
			ContentValues values = new ContentValues();
			if(amount ==0) amount = 1;
			values.put(COLUMN_ITEM, name);
			values.put(COLUMN_VALOR, valor);
			values.put(COLUMN_BARCOD, barcode);
			values.put(COLUMN_IMAGE, uri);		
			values.put(COLUMN_QUANT,amount);
			values.put(COLUMN_DTCOMPRA, localDate.toString());
			calcId = dd.insertOrThrow(TB_NAME, null, values);

			dd.setTransactionSuccessful();

		} catch (Exception e) {
			Log.e("SQLite", e.getMessage(), e);
		} finally {
			if (dd.isOpen()) {
				dd.endTransaction();
			}
		}
		return calcId;
	}

	// ATUALIZANDO DADOS........
	public long updateItem(String name, float valor, String photoUri, int id, int q) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		long calcId = 0;
		try {
			ContentValues values = new ContentValues();
			if (name != null)
				values.put(COLUMN_ITEM, name);
			if (valor != 0)
				values.put(COLUMN_VALOR, valor);
			if (q != 0)
				values.put(COLUMN_QUANT, q);
			if (photoUri != null && !photoUri.isEmpty())
				values.put(COLUMN_IMAGE, photoUri);
			calcId = db.update(TB_NAME, values, "id = ?", new String[] { String.valueOf(id) });
			db.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			db.endTransaction();
		}
		return calcId;
	}

	//REMOVER DADOS....
	public long removeItem(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		long calcId = 0;
		try {
			calcId = db.delete(TB_NAME, " id = ?", new String[] { String.valueOf(id) });
			db.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			db.endTransaction();
		}
		return calcId;
	}

}
