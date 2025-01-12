package com.rogger.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ActivityPrincipal extends BaseActivity {
	private int ano = 0;
	private int cont = 0;
	private int day;
	private int quant;
	private TextView txt1bs, tx2tbs;
	private Controlador controlador;
	private boolean click = false;
	private BottomNavigationView bottonNav;
	private ListView listView;
	private NavigationView nav;
	private BottomSheetBehavior<View> bottomSheet;
	private ArrayAdapter<String> adpterHeader;
	private ArrayList<String> itemsHeader;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main_with_drawer);

		OpenHelper.getInstance(this).removeItem(6);

		FloatingActionButton btbFb = findViewById(R.id.fab);
		FloatingActionButton btbHtml = findViewById(R.id.fab_html);

		UseControl use = new UseControl();
		nav = findViewById(R.id.nav_view);
		ImageView imgUp = findViewById(R.id.img_up);
		txt1bs = findViewById(R.id.txt1_BottomShee);
		tx2tbs = findViewById(R.id.txt2_BottomShee);
		View bottomSheetView = findViewById(R.id.cvBottomSheet);
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		bottonNav = findViewById(R.id.bottomAppBar);
		//View headerView = nav.getHeaderView(0);
		use.setNav(nav, this);
		use.getNav();

		//listView = headerView.findViewById(R.id.list_item_header);
		addMenuItem("Queijos", R.id.menu_2, 100);

		btbFb.setOnClickListener(this::startCadastro);
		btbHtml.setOnClickListener(this::startHtml);

		List<Registro> dados = OpenHelper.getInstance(this).getRegisterBy();
		ArrayList<Fragment> flagList = new ArrayList<>();
		ArrayList<String> listMes = new ArrayList<>();
		TreeMap<String, List<Registro>> registroOrdenados = new TreeMap<>();

		for (Registro registro : dados) {
			LocalDate data = LocalDate.parse(registro.data);
			String chaveMesAno = makeDateString(data.getMonthValue(), data.getYear());
			// Adiciona ao mapa ordenado
			registroOrdenados.computeIfAbsent(chaveMesAno, k -> new ArrayList<>()).add(registro);
		}
		// Processa os registros ordenados
		for (Map.Entry<String, List<Registro>> entry : registroOrdenados.entrySet()) {
			String mesAno = entry.getKey();
			List<Registro> itens = entry.getValue();
			// Adiciona o título do mês e ano
			listMes.add(mesAno);
			// Cria o fragment para o mês atual			
			flagList.add(FragmentCustom.newInstance(itens));
		}
		// Configura o ViewPager
		ViewPager viewPager = findViewById(R.id.viewPager_TabsScrolledActivity);
		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), flagList, listMes));
		TabLayout tabLayout = findViewById(R.id.tabLayout_TabsScrolledActivity);
		tabLayout.setupWithViewPager(viewPager, true);

		bottonNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
				case R.id._navigation_menu:
					drawer.openDrawer(GravityCompat.START);
					return true;			
				case R.id._menu_bottom_sheet:
				    updateBottomSheet();
					return true;				
				case R.id.add_bottom_sheet:
					startCadastro();
					return true;			
				default:
				return true;			
				}				
			}
		});
		bottomSheet = BottomSheetBehavior.from(bottomSheetView);
		bottomSheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(View bottomsheet, int newState) {
				switch (newState) {
				case BottomSheetBehavior.STATE_COLLAPSED:
					imgUp.animate().setDuration(300).rotation(0f);
					break;
				case BottomSheetBehavior.STATE_EXPANDED:
					imgUp.animate().setDuration(300).rotationBy(180f);
					txt1bs.setText("Total gasto nesse mes é :" + String.format("%.2f", 5.899) + "€");
					tx2tbs.setText("Total de items comprado nesse mês é :" + 12.45);
					break;
				case BottomSheetBehavior.STATE_HIDDEN:
				case BottomSheetBehavior.STATE_DRAGGING:
				case BottomSheetBehavior.STATE_HALF_EXPANDED:
				case BottomSheetBehavior.STATE_SETTLING:
					break;
				}
			}

			@Override
			public void onSlide(View arg0, float arg1) {
			}
		});

	}

	private void startCadastro(View view) {
		startActivity(new Intent(this, CameraScanActivity.class));
	}

	private void startHtml(View view) {
		startActivity(new Intent(this, HtmlBuscar.class));
	}

	private String makeDateString(int month, int year) {
		return getMonthFormat(month) + "-" + year;
	}

	private String getMonthFormat(int i) {
		return i == 1 ? "Jan"
				: i == 2 ? "Fev"
						: i == 3 ? "Mar"
								: i == 4 ? "Abr"
										: i == 5 ? "Mai"
												: i == 6 ? "Jun"
														: i == 7 ? "Jul"
																: i == 8 ? "Ago"
																		: i == 9 ? "Set"
																				: i == 10 ? "Out"
																						: i == 11 ? "Nov"
																								: i == 12 ? "Dez"
																										: "Jan";
	}

	private void addMenuItem(String title, int groupId, int itemId) {
		Menu menu = nav.getMenu();
		menu.add(groupId, itemId, Menu.NONE, title);
		//.setIcon(R.drawable.ic_menu) para deginir um icon opcional
	}

	public void updateBottomSheet() {
		switch (bottomSheet.getState()) {
		case BottomSheetBehavior.STATE_COLLAPSED:
			bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
			break;
		case BottomSheetBehavior.STATE_EXPANDED:
			bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
			break;
		}
	}

}
