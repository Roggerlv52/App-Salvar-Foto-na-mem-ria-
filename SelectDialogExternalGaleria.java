package com.rogger.test;

import android.content.DialogInterface;
import android.content.Context;

public final class SelectDialogExternalGaleria implements DialogInterface.OnClickListener {

	public final Context f$0;

	public SelectDialogExternalGaleria(Context context) {
		this.f$0 = context;
	}

	public final void onClick(DialogInterface dialogInterface, int i) {
		Dialog.abrirGaleria(this.f$0);
	}

}