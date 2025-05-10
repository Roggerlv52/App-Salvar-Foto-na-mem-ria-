package com.rogger.test;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SetDataC {		
	
	private String name = "";
	private int amount;
	private float price;
	private float soma;
	private String barcode = "";
	
	public SetDataC() {

	}
	public void setEdtName(EditText edt) {
		this.name = edt.getText().toString();
	}

	public void setEdtAmount(EditText edt) {
		int q = 0;
		try {
			q = Integer.parseInt(edt.getText().toString());
		} catch (Exception e) {
			q = 0;
		}
		this.amount = q;
	}
	public void setEdtPrice(EditText edt) {
		float f = 0;
		try {
			if(amount == 0){
				f += Float.parseFloat(edt.getText().toString());
			}
			for (int i = 1; i <= amount; i++) {
				f += Float.parseFloat(edt.getText().toString());
			}
			
		} catch (Exception e) {
			//Toast.makeText(context, "Preço invalida! -> " + edt.getText(), Toast.LENGTH_SHORT).show();
			this.price = 0;
		}
		this.price = f;
	}
	public int getAmount() {
		return this.amount;
	}
	public String getName() {
		return this.name;
	}

	public void setBarcode(String b) {
		this.barcode = b;
	}

	public String getBarcode() {
		return this.barcode;
	}
}
