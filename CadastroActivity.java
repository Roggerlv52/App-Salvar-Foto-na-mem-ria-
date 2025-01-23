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
import androidx.core.graphics.BitmapCompat;
import com.google.android.material.textfield.TextInputLayout;
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
	private TextInputLayout input;
	private ImageButton btnBarcode, tobackMain;
	private ImageView imageView;
	private String currentPhotoPath, photo, barcodeCameraScan;
	private EditText edtName, edtQuant, edtValor;
	private TextView txt2, txt3, txtTotValor;
	private int txtvisible;
	private Uri photoUri;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_cadastro);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle((CharSequence) "Cadastrar novo item");
		}
		edtName = findViewById(R.id.edt_cadastro_name);
		edtQuant = findViewById(R.id.edt_cadastro_q);
		txt2 = findViewById(R.id.txt_2);
		input = findViewById(R.id.inputLayoutError);
		txt3 = findViewById(R.id.txt_cadastro_3);
		txtTotValor = findViewById(R.id.txt_cadastro_tot);
		edtValor = findViewById(R.id.edt_cadastro_valor);
		tobackMain = findViewById(R.id.main_back);
		btnBarcode = findViewById(R.id.btn_scan);
		imageView = findViewById(R.id.img_cadastro);

		Intent getIntentCameraScan = getIntent();
		if (getIntentCameraScan != null) {
			barcodeCameraScan = getIntentCameraScan.getStringExtra("cameraScanActivity");
			txt3.setText(barcodeCameraScan);
		}
		SetDataC set = new SetDataC(this);   
		startPermission(this);
		new Dialog(this);
		tobackMain.setOnClickListener(v ->{
			tobackMain.setOnClickListener(v -> {
			set.setEdtName(edtName);
			set.setEdtAmount(edtQuant);
			set.setEdtPrice(edtValor);

			if (validate(set.getName(), set.getTotPrice(), set.getAmount())) {
				long additem = 0;
				try {
					additem = OpenHelper.getInstance(this).additem(set.getName(), set.getTotPrice(), barcodeCameraScan, set.getAmount(),
							currentPhotoPath);
					if (additem > 0) {
						Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Banco de dados nao esta gravando!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception unused) {
					Toast.makeText(this, "nenhum dado salvo", Toast.LENGTH_SHORT).show();
					finish();
				}
				startActivity(new Intent(this, ActivityPrincipal.class));
				finish();
			}
			
		});
		imageView.setOnClickListener(this::startDialogOp);			
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
//Funcão para reduzir o bitmap
	private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) maxWidth) / width;
		float scaleHeight = ((float) maxHeight) / height;
		float scaleFactor = Math.min(scaleWidth, scaleHeight);

		int newWidth = Math.round(width * scaleFactor);
		int newHeight = Math.round(height * scaleFactor);

		return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
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
		   try {
				Bitmap originalBitmap;

				if (data != null) {
					// Seleção da galeria
					InputStream inputStream = getContentResolver().openInputStream(data.getData());
					originalBitmap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();
				} else {
					// Foto capturada
					originalBitmap = BitmapFactory.decodeFile(currentPhotoPath);
				}
				// Redimensiona a imagem para baixa resolução
				Bitmap resizedBitmap = resizeBitmap(originalBitmap, 1024, 1024); // Ajuste os valores conforme necessário

				// Salva a imagem redimensionada em um novo arquivo
				File resizedImageFile = createImageFile();
				FileOutputStream outputStream = new FileOutputStream(resizedImageFile);
				resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // Ajuste a qualidade, se necessário
				outputStream.close();

				currentPhotoPath = resizedImageFile.getAbsolutePath();
				// Exclui a imagem original de alta resolução
				if (data == null) { // Imagem capturada (não vinda da galeria)
					File originalFile = new File(currentPhotoPath);
					if (originalFile.exists()) {
						photoFile.delete();
					}
				}
				// Carrega a imagem no ImageView
				Picasso.get().load(resizedImageFile).into(imageView);
			} catch (IOException e) {
				e.printStackTrace();
		   }	
		}
	}
	private boolean validate(String name, float v, int q) {
		if (name == null || name.isEmpty()) {
			input.setError("Caractere nome precisa ser preenchido!");
			return false;
		}
		if (v == 0.00f && q == 0) {
			Toast.makeText(this, "Erro: valor nao pode ser Zero! " + v, Toast.LENGTH_SHORT).show();
			return false;
		}
		input.setError(null);
		return true;
	}
	@Override
	public void StarCamera() {
		startPermissionCamera();		
	}
}
