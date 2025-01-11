package com.rogger.test;

import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (Build.VERSION.SDK_INT >= 21) {
			int i = getResources().getConfiguration().uiMode & 48;
			if (i == 0) {
				// setStatusBarIconColor(true);
			} else if (i == 16) {
				getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
				if (Build.VERSION.SDK_INT >= 26) {
					// setStatusBarIconColor(true);
					getWindow().getDecorView().setSystemUiVisibility(16);
				}
			} else if (i == 32) {
				//setStatusBarIconColor(false);
				getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
			}
		}
	}
        protected void startPermission(Context context) {
		if (ContextCompat.checkSelfPermission(context,
			Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context,
			new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
		}
        }
}
