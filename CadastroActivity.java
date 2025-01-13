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

public class CadastroActivity extends BaseActivity implements Controlador{
	private static final int REQUEST_CAMERA_PERMISSION = 100;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageButton btnBarcode, tobackMain;
	private ImageView imageView;
	private String currentPhotoPath, photo, barcodeCameraScan;
	private EditText edtName, edtQuant, edtValor;
	private TextView txt2, txt3, txtTotValor;
	private int txtvisible;
	private Uri photoUri;
	private float soma, valor;

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
		 SetDataC set = new SetDataC(this,input);
		set.setEdtPrice(edtValor);
		valor = set.getTotPrice();
		String name = set.getName();
	    
		startPermission(this);
		new Dialog(this);
		tobackMain.setOnClickListener(v ->{
			if(name.length() <4){
				input.setError("Nome deve ser maior que 3!");				
			}else {
				input.setHelperTextEnabled(false);
			}
		});
		imageView.setOnClickListener(this::startDialogOp);
			
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
		long additem = OpenHelper.getInstance(this).additem(str, f, str2, i, currentPhotoPath);
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
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Exclui o arquivo antigo, se existir
			if (photoFile != null && photoFile.exists()) {
				photoFile.delete();
			}
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (photoFile != null) {
				photoUri = FileProvider.getUriForFile(CadastroActivity.this,
						getApplicationContext().getPackageName() + ".fileprovider", photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	private File createImageFile() throws IOException {
		// Cria o nome do arquivo da imagem
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, // prefixo
				".jpg", // sufixo
				storageDir);
		// Guarda o caminho do arquivo para usá-lo depois
		currentPhotoPath = image.getAbsolutePath();
		return image; // diretório
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {			
			if (data != null) {
				Uri imageUri = data.getData();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // API 28 ou superior
					try {
						ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
						Bitmap bitmap = ImageDecoder.decodeBitmap(source);
						imageView.setImageBitmap(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else { // API 27 ou inferior (fallback para o método antigo)
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
						imageView.setImageBitmap(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{				
				imageView.setImageURI(photoUri);
			}
			
		}
	}
	@Override
	public void StarCamera() {
		startPermissionCamera();		
	}
}
