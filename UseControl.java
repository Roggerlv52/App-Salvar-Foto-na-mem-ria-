package com.rogger.test;

import android.content.Context;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class UseControl {
	private Context context;
	private Controlador control;
	private NavigationView navigationView;
	private String dados;

	public UseControl() {		
	}

	public void setNav(NavigationView n,Context c) {
		this.navigationView = n;
		this.context = c;
	}

	public boolean getNav() {
		navigationView.setNavigationItemSelectedListener(item -> {
			if (item.isCheckable()) {
				item.setChecked(true);
			}
			switch (item.getItemId()) {
			case R.id.nav_info:
				//	Toast.makeText(this, "clicado", Toast.LENGTH_SHORT).show();
				Dialog.ShowDialog(context);
				break;
			default:
				Toast.makeText(context, "id- " + item.getItemId(), Toast.LENGTH_SHORT).show();
			}
			return true;
		});
		return true;
	}

	public void setDados(String d) {
		this.dados = d;
	}

	public String getDados() {
		return this.dados;
	}

}