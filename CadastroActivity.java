package com.rogger.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CadastroActivity extends BaseActivity {
	private static final int REQUEST_CAMERA_PERMISSION = 100;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageButton btnBarcode, tobackMain;
	private ImageView imageView;
	private String currentPhotoPath, photo, barcodeCameraScan;
	private EditText edtName, edtQuant, edtValor;
	private TextView txt2, txt3, txtTotValor;
	private int txtvisible;
	private Uri photoUri;
	float soma, valor;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_cadastro);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle((CharSequence) "Cadastrar novo item");
		}

		this.edtName = findViewById(R.id.edt_cadastro_name);
		this.edtQuant = findViewById(R.id.edt_cadastro_q);
		this.txt2 = findViewById(R.id.txt_2);
		this.txt3 = findViewById(R.id.txt_cadastro_3);
		this.txtTotValor = findViewById(R.id.txt_cadastro_tot);
		this.edtValor = findViewById(R.id.edt_cadastro_valor);
		this.tobackMain = findViewById(R.id.main_back);
		this.btnBarcode = findViewById(R.id.btn_scan);
		this.imageView = findViewById(R.id.img_cadastro);

		Intent getIntentCameraScan = getIntent();
		if (getIntentCameraScan != null) {
			barcodeCameraScan = getIntentCameraScan.getStringExtra("cameraScanActivity");
			txt3.setText(barcodeCameraScan);
		}
		this.edtValor.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String obj = CadastroActivity.this.edtValor.getText().toString();
				if (obj.isEmpty()) {
					CadastroActivity.this.valor = 0.0f;
					return;
				}
				CadastroActivity.this.valor = Float.parseFloat(obj);
			}
		});
		this.edtQuant.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int i = 0;
				CadastroActivity.this.txt2.setVisibility(0);
				CadastroActivity.this.txtTotValor.setVisibility(0);
				String obj = CadastroActivity.this.edtQuant.getText().toString();
				if (!obj.isEmpty()) {
					CadastroActivity.this.soma = 0.0f;
					i = Integer.parseInt(obj);
					for (int i2 = 1; i2 <= i; i2++) {
						CadastroActivity.this.soma += CadastroActivity.this.valor;
					}
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					CadastroActivity.this.txtTotValor
							.setText("€" + decimalFormat.format((double) CadastroActivity.this.soma));
				}
				if (i < 2) {
					CadastroActivity.this.txt2.setVisibility(4);
					CadastroActivity.this.txtTotValor.setVisibility(4);
				}
			}
		});
		this.tobackMain.setOnClickListener(this::setClickSaveData);
		this.imageView.setOnClickListener(this::startDialogOp);
	}

	private void setClickSaveData(View view) {
		String obj = this.edtName.getText().toString();
		try {
			SalvarData(obj, this.soma, barcodeCameraScan, Integer.parseInt(this.edtQuant.getText().toString()));
		} catch (Exception unused) {
			Toast.makeText(this, "nenhum dado salvo", 0).show();
			finish();
		}
	}

	private void SalvarData(String str, float f, String str2, int i) {
		long additem = OpenHelper.getInstance(this).additem(str, f, str2, i, this.photoUri.toString());
		if (additem > 0) {
			Toast.makeText(this, "Salvo com sucesso!" + additem, 0).show();
		} else {
			Toast.makeText(this, "Banco de dados nao esta gravando!", 0).show();
		}
		startActivity(new Intent(this, ActivityPrincipal.class));
		finish();
	}

	private void startPermissionCamera(View view) {
		if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0
				&& ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
			openCamera();
		} else {
			ActivityCompat.requestPermissions(this,
					new String[] { "android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE" }, 100);
		}
	}

	private void startCameraScan(View view) {
		startActivity(new Intent(this, CameraScanActivity.class));
	}

	private void openCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if (intent.resolveActivity(getPackageManager()) != null) {
			File file = null;
			try {
				file = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (file != null) {
				Uri uriForFile = FileProvider.getUriForFile(this,
						getApplicationContext().getPackageName() + ".fileprovider", file);
				this.photoUri = uriForFile;
				intent.putExtra("output", uriForFile);
				startActivityForResult(intent, 1);
			}
		}
	}

	private File createImageFile() throws IOException {
		String format = new SimpleDateFormat("yyyyMMdd").format(new Date());
		File createTempFile = File.createTempFile("JPEG_" + format + "_", ".jpg",
				getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		this.currentPhotoPath = createTempFile.getAbsolutePath();
		return createTempFile;
	}

	public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
		super.onRequestPermissionsResult(i, strArr, iArr);
		if (i == 100 && iArr.length > 0 && iArr[0] == 0) {
			openCamera();
		}
	}
	private void startDialogOp(View v){
		Dialog.dialogOpicion(CadastroActivity.this);
	}

}