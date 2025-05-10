package com.rogger.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;

public class CameraScanActivity extends AppCompatActivity {
	private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
	private BarcodeView cameraView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_scan);

		cameraView = findViewById(R.id.camera_view);
		TextView txtBtn = findViewById(R.id.camera_scan_btn);

		// Verificar permissão de câmera
		if (hasCameraPermission()) {
			cameraView.setDecoderFactory(new DefaultDecoderFactory(
					Arrays.asList(new BarcodeFormat[] { BarcodeFormat.RSS_EXPANDED, BarcodeFormat.EAN_13 })));
			cameraView.decodeContinuous(new BarcodeCallback() {
				@Override
				public void barcodeResult(BarcodeResult arg0) {
					//Toast.makeText(CameraScanActivity.this, "código" + arg0, Toast.LENGTH_SHORT).show();
					if (arg0.getText() != null) {
						startCadastro(arg0.toString());
					}
				}
			});
		} else {
			requestCameraPermission();
		}
		txtBtn.setOnClickListener(this::startDialogCan);
	}

	private void startDialogCan(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater.
		LayoutInflater inflater = getLayoutInflater();

		// Inflate and set the layout for the dialog.
		// Pass null as the parent view because it's going in the dialog layout.
		View dialogView = inflater.inflate(R.layout.layout_dialog, null);

		TextView txtDialog = dialogView.findViewById(R.id.txt_dialog);
		txtDialog.setText("Digite código de barras manual");

		ImageView imgDialog = dialogView.findViewById(R.id.img_dialog);
		imgDialog.setImageResource(R.drawable.ic_barcode_24);

		EditText editText = dialogView.findViewById(R.id.edt_dialog);
		editText.setVisibility(View.VISIBLE);

		builder.setView(dialogView)
				// Add action buttons

				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						//dataC.setBarcode(editText.getText()+"");
						startCadastro(editText.getText().toString());
					}
				}).setNeutralButton("Canselar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//LoginDialogFragment.this.getDialog().cancel();
						finish();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void startCadastro(String code) {
		Intent intent = new Intent(CameraScanActivity.this, CadastroActivity.class);
		intent.putExtra("cameraScanActivity", code);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (hasCameraPermission()) {
			this.cameraView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.cameraView.pause();
	}

	private boolean hasCameraPermission() {
		return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
	}

	private void requestCameraPermission() {
		ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
				CAMERA_PERMISSION_REQUEST_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//fotoapparat.start();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
