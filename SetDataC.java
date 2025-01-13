package com.rogger.test;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SetDataC {
	
	private EditText edtName, edtQuant, edtPrice;
	private Context context;
	private String name = "";
	private int amount;
	private float price;
	private String barcode = "";
	
	public SetDataC() {

	}
	public void setEdtName(EditText edt) {
		this.name = edt.getText().toString();
	}

	public void setEdtAmount(EditText edt) {

		this.amount = Integer.parseInt(edt.getText().toString());
	}
	public void setEdtPrice(EditText edt) {
		edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String obj = edt.getText().toString();
				if (obj.isEmpty()) {
					price = 0.0f;
					return;
				}
				price = Float.parseFloat(obj);
			}

		})
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
