package com.rogger.test;

import android.content.Context;

public class SetDataC {
	
	private Context context;
	private String name = "";
	private String barcode ="";
	
	public SetDataC(){
		
	}
	public void setContext(Context c){
		this.context = c;
	}
	public void setBarcode(String b){
		this.barcode = b;
	}
	public String getName(){
		return this.name;
	}
	public String getBarcode(){
		return this.barcode;
	}

}