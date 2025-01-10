package com.rogger.test;

import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import org.jsoup.select.Elements;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlBuscar extends AppCompatActivity {
	private TextView txt;
	private WebView webview;
	private String PREVISAOTEMPO = "https://www.ipma.pt/pt/otempo/prev.localidade.hora/index.jsp#Coimbra&Cantanhede";
	private String DOLARHOJE = "https://dolarhoje.com/";
	private String SITEANIME = "https://animescomix.tv/7652/";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.html_buscar);

		txt = findViewById(R.id.txt_html);
		webview = findViewById(R.id.web_view);

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);

		//Força o webView abrir links dentro dele mesmo
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				String url = request.getUrl().toString();
				//Broqueia os anuncios conhecidos 
				if (url.contains("ads") || url.contains("doubleclick.net") || url.contains("adservice.google.com")) {
					return new WebResourceResponse("text/plain", "utf-8", null);
				}
				return super.shouldInterceptRequest(view, request);
			}

		});

		webview.loadUrl(SITEANIME);

		//fetchDollarPrice();
	}

	@Override
	public void onBackPressed() {
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			super.onBackPressed();
		}
	}

	private void fetchDollarPrice() {
		// Executor para rodar em thread separada
		ExecutorService executor = Executors.newSingleThreadExecutor();
		// Handler para atualizar na thread principal
		Handler handler = new Handler(Looper.getMainLooper());
		executor.execute(() -> {
			try {
				// Conecta ao site e obtém o documento HTML
				Document doc = Jsoup.connect("https://dolarhoje.com/").get();

				// Seleciona o campo do valor do dólar (input com id="nacional")
				Elements elements = doc.select("input#nacional");

				// Obtém o valor do atributo 'value'
				String valor = elements.attr("value");

				// Atualiza na thread principal
				handler.post(() -> txt.setText("Cotação do dólar: R$ " + valor)); //txt.setText("HTML- "+doc));

			} catch (IOException e) {
				e.printStackTrace();
				// Mostra um erro na interface
				handler.post(
						() -> Toast.makeText(HtmlBuscar.this, "Erro ao buscar cotação!", Toast.LENGTH_SHORT).show());
			}
		});
	}
}