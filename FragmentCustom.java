package com.rogger.test;

import android.content.Intent;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.ListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentCustom extends Fragment {
	public interface OnDataPass {
		void onDataReceived(String data);
	}

	private static final String TITLE_KEY = "title";
	
	private ArrayList<Registro> itemList;	
	private float soma = 0;
	private int somQuant = 0;
	private int iD;
	private Registro d;

	@Override
	public void onCreate(Bundle args) {
		super.onCreate(args);

		Bundle bundle = getArguments();
		if (bundle != null) {
			itemList = bundle.getParcelableArrayList(TITLE_KEY);
			// Use a lista como necessário			
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup conteiner, Bundle arg2) {
		return inflater.inflate(R.layout.fragment_custom, conteiner, false);
	}

	@Override
	public void onViewCreated(View view, Bundle arg1) {
		super.onViewCreated(view, arg1);

		ListView listView = view.findViewById(R.id.list_item_Fragment);
	
		ArrayList<String> itensFormatados = new ArrayList<>();

		int cont = 0;
		for (Registro item : itemList) {
			cont++;
			itensFormatados.add(cont + "- " + item.name + " Data: " + item.data);
			item.setId(item.id);
			soma += item.preco;
			somQuant += item.quantidade;
		}

		//String fomatado = String.format("%.2f",soma);
		//Passando dados para criar uma listagem de itens
		if (itemList != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
					itensFormatados);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener((parent, view1, position, id) -> {
				Registro d = itemList.get(position);
				//Toast.makeText(requireContext(), d.id + " id", Toast.LENGTH_SHORT).show();
				irpara(d.getId());
			});
		}		
	}

	private void irpara(int id) {
		Intent intent = new Intent(getActivity(), DetalhesActivity.class);
		//intent.addFlags(Intent.)
		intent.putExtra("intId", id);
		startActivity(intent);
		//getActivity().finish();
	}

	public static FragmentCustom newInstance(List<Registro> itemList) {
		FragmentCustom fragment = new FragmentCustom();
		Bundle args = new Bundle();
		args.putParcelableArrayList(TITLE_KEY, new ArrayList<>(itemList));
		fragment.setArguments(args);
		return fragment;
	}

		

}