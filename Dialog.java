package com.rogger.test;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

public class Dialog {
	private static AlertDialog create;
	private static EditText ed_categore;
	private static ImageView cancel, accept;

	public static void ShowDialog(Context context) {

		View inflate = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
		//String string = SharedPreferencesManager.getString(context, "chave", "0");
		ed_categore = inflate.findViewById(R.id.et_new_categore_cusd);
		cancel = inflate.findViewById(R.id.cancel_custon_dialog);
		accept = inflate.findViewById(R.id.ok_custon_dialog);

		accept.setOnClickListener(v -> {
			Toast.makeText(context, "Aceito", Toast.LENGTH_SHORT).show();
			create.dismiss();
		});
		cancel.setOnClickListener(v -> {
			Toast.makeText(context, "Cancelar", Toast.LENGTH_SHORT).show();
			create.dismiss();
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(inflate);
		builder.setTitle("Categoria");

		create = builder.create();
		create.show();
	}

	public static void dialogOpicion(Context context) {
		View inflate = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
		//String string = SharedPreferencesManager.getString(context, "chave", "0");
		ed_categore = inflate.findViewById(R.id.et_new_categore_cusd);
		cancel = inflate.findViewById(R.id.cancel_custon_dialog);
		accept = inflate.findViewById(R.id.ok_custon_dialog);
		TextView txtdialog = inflate.findViewById(R.id.tx_new_category);

		ed_categore.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		accept.setVisibility(View.GONE);
		txtdialog.setVisibility(View.GONE);
		//Toast.makeText(context, "Aceito", Toast.LENGTH_SHORT).show();
		//create.dismiss();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder
				//.setTitle("Selecione um opção.")
				//.setIcon(R.drawable.ic_camera_24)
				.setMessage("Escolha um opição.").setPositiveButton("Galeria", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				}).setNeutralButton("Camera", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});

		builder.setView(inflate);
		create = builder.create();
		create.show();
	}
}
