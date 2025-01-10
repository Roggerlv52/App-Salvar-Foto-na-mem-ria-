package com.rogger.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Config;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.util.List;

public class DetalhesActivity extends AppCompatActivity {
	private int id = 0;
	private AppBarLayout appBarLayout;
	private Bitmap bitmapImg;
	private ImageView imgBarcode;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//	if (getSupportActionBar() != null) {
		//		getSupportActionBar().setTitle("Detalhes");
		//	}
		setContentView(R.layout.activity_detalhes);
		appBarLayout = findViewById(R.id.appBar_ToolbarCollapsedActivity);
		CollapsingToolbarLayout collpsingToolbar = findViewById(R.id.collapsing_toolbar);

		collpsingToolbar.setCollapsedTitleTextColor(Color.BLACK);

		TextView txtUri = findViewById(R.id.txt_uri_detalhes);
		ImageView img = findViewById(R.id.image_detalhes);
		imgBarcode = findViewById(R.id.img_barcode_detalhes);
		TextView txtBarcode = findViewById(R.id.txt_barcode_detalhes);
		TextView txtName = findViewById(R.id.txt_name_detalhes);
		TextView txtValor = findViewById(R.id.txt_valor_detalhes);
		TextView txtData = findViewById(R.id.txt_data_detalhes);
		TextView txtQuant = findViewById(R.id.txt_quant_detalhes);
		Button btnDelete = findViewById(R.id.btn_delete_detalhes);

		Intent intent = getIntent();
		if (intent != null) {

			id = intent.getIntExtra("intId", 0);
		}

		Registro dados = OpenHelper.getInstance(this).buscarId(id);
		String stringUri = dados.uri;
		if (stringUri != null && !stringUri.isEmpty()) {
			img.setImageURI(Uri.parse(stringUri));
		} else {
			img.setImageResource(R.drawable.ic_no_image);
		}

		txtUri.setText(dados.uri);
		txtName.setText(dados.name);
		txtValor.setText("Valor: " +String.format("%.2f",dados.preco)+ "€");
		txtBarcode.setText(dados.barcode);
		txtQuant.setText("Quantidade: " + dados.quantidade);
		txtData.setText("Data: " + dados.data);
		bitmapImg = ImageBarcod(dados.barcode);

		// Adiciona um listener para verificar o estado
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				// Verifica se está expandido
				if (verticalOffset == 0) {
					// AppBar está totalmente expandido
					collpsingToolbar.setTitle("");
					if (bitmapImg != null) {
						//imgBarcode.setImageBitmap(bitmapImg);
						imgBarcode.setImageBitmap(bitmapImg);

					} else {
						imgBarcode.setImageResource(R.drawable.ic_no_image);
					}

				} else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
					// AppBar está totalmente colapsado
					collpsingToolbar.setTitle("Detalhes");
				} else {
					// AppBar está entre expandido e colapsado
					//Log.d("AppBarState", "Intermediate");
				}
			}
		});
		btnDelete.setOnClickListener(v -> {
			closeActivity(id, stringUri);
		});
	}

	private Bitmap ImageBarcod(String str) {
		Bitmap bitmap = null;
		try {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
			//	this.imgBarcode.setImageBitmap(new BarcodeEncoder().encodeBitmap(str, BarcodeFormat.CODE_128, 400, 200));
			bitmap = barcodeEncoder.encodeBitmap(str, BarcodeFormat.CODE_128, 400, 200);
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), 0).show();
		}
		return bitmap;
	}

	private void closeActivity(int id, String dados) {
///storage/emulated/0/Android/data/com.rogger.test/files/Pictures/JPEG_20241201_4591455540185839220.jpg
		File file = new File(dados);
		//	String absolutePath = file.getAbsolutePath();
		//	File fileimg = getExternalFilesDir(absolutePath);

		// Verifica se o arquivo existe antes de tentar deletá-lo
		if (file.exists()) {
			boolean deleted = file.delete();
			if (deleted) {
				//Log.d("ImageDeletion", "Imagem deletada com sucesso: " + imagePath);
				Toast.makeText(this, "Img Excluindo com sucesso! ", Toast.LENGTH_SHORT).show();
				long idD = OpenHelper.getInstance(this).removeItem(id);
				if (idD > 0) {

					Intent i = new Intent(DetalhesActivity.this, ActivityPrincipal.class);
					startActivity(i);
					finish();
				}
			} else {
				// Log.e("ImageDeletion", ": " + imagePath);
				Toast.makeText(this, "Falha ao deletar a imagem! ", Toast.LENGTH_SHORT).show();
			}
		} else {
			// Log.e("ImageDeletion", "Arquivo não encontrado: " + imagePath);
			Toast.makeText(this, "" + file, Toast.LENGTH_LONG).show();
		}

	}
}