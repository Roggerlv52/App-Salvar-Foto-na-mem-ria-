package com.rogger.test;

import android.os.Parcel;
import android.os.Parcelable;
import com.rogger.test.Registro;

public class Registro implements Parcelable {
	int id;
	String name;
	String barcode;
	String uri;
	String data;
	float preco;
	int quantidade;

	public Registro(int id, String name, float valor, int q, String barcode, String uri, String data) {
		this.id = id;
		this.name = name;
		this.preco = valor;
		this.barcode = barcode;
		this.quantidade = q;
		this.data = data;

	}

	public Registro() {

	}
	public void setId(int id){
		this.id = id;
	} 
	public int getId(){
		return this.id;
	}

	// Métodos Parcelable
	protected Registro(Parcel in) {
		id = in.readInt();
		name = in.readString();
		preco = in.readFloat();
		barcode = in.readString();
		quantidade = in.readInt();
		data = in.readString();
	}

	public static final Creator<Registro> CREATOR = new Creator<Registro>() {
		@Override
		public Registro createFromParcel(Parcel in) {
			return new Registro(in);
		}

		@Override
		public Registro[] newArray(int size) {
			return new Registro[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
         dest.writeInt(id);
		 dest.writeString(name);
		 dest.writeFloat(preco);
		 dest.writeString(barcode);
		 dest.writeInt(quantidade);
		 dest.writeString(data);
	}

}