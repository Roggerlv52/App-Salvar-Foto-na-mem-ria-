package com.rogger.test;

import java.time.LocalDate;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.Calendar;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
	private final ArrayList<Fragment> listFragment;
	private final ArrayList<String> listTitle;
	private int ano, mes = 0;

	public PagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragment,ArrayList<String> listTitle) {
		super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
		this.listFragment = listFragment;
		this.listTitle = listTitle;
	}

	@Override
	public int getCount() {
		return listFragment.size();
	}

	@Override
	public Fragment getItem(int position) {
		return listFragment.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return listTitle.get(position);
	}

}
