package com.arcao.menza.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arcao.menza.R;

import java.lang.ref.WeakReference;

public class NavigationDrawerFragment extends Fragment {
	private static final String TAG = "NavigationDrawerFragment";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private SharedPreferences mPrefs;
	private View mFragmentContainerView;
	private DrawerLayout mDrawerLayout;
	private ArrayAdapter<CharSequence> mAdapterPlaces;
	private ListView mListPlaces;
	private WeakReference<OnDrawerCallbackListener> listenerRef;
	private boolean userLearnedDrawer;

	public interface OnDrawerCallbackListener {
		public void placeSelected(int placeId);
	}

	public void setup(int drawerFragmentContainer, DrawerLayout drawerLayout, Toolbar toolbar) {
		mDrawerLayout = drawerLayout;
		Toolbar mToolbar = toolbar;

		mFragmentContainerView = getActivity().findViewById(drawerFragmentContainer);

		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
						getActivity(),  mDrawerLayout, mToolbar,
						R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!userLearnedDrawer) {
					userLearnedDrawer = true;
					mPrefs.edit().putBoolean(PREF_USER_LEARNED_DRAWER, userLearnedDrawer).commit();
				}
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (!userLearnedDrawer) {
			openDrawer();
		}

		mDrawerToggle.syncState();
	}

	public void setPlaceId(int placeId) {
		mListPlaces.setItemChecked(placeId, true);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		userLearnedDrawer = mPrefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		try {
			listenerRef = new WeakReference<>((OnDrawerCallbackListener) getActivity());
		} catch (ClassCastException e) {
			Log.e(TAG, "The activity has to implement OnDrawerCallbackListener!", e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, true);

		preparePlaces(view);

		return view;
	}

	@Override
	public void onDestroyView() {
		mFragmentContainerView = null;
		super.onDestroyView();
	}

	private void preparePlaces(View view) {
		mAdapterPlaces = ArrayAdapter.createFromResource(getActivity(), R.array.places, R.layout.drawer_place_item);

		mListPlaces = (ListView) view.findViewById(R.id.drawer);
		mListPlaces.setAdapter(mAdapterPlaces);
		mListPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListPlaces.setItemChecked(position, true);
				OnDrawerCallbackListener listener = listenerRef.get();
				if (listener != null) {
					listener.placeSelected(position);
				}
				closeDrawer();
			}
		});
	}

	public CharSequence getPlaceName(int placeId) {
		return mAdapterPlaces.getItem(placeId);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}


	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}
}
