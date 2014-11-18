package com.arcao.menza.fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arcao.menza.R;
import com.arcao.menza.adapter.DrawerListAdapter;
import com.arcao.menza.util.SharedPreferencesCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {
	private static final String TAG = "NavigationDrawerFragment";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private SharedPreferences mPrefs;
	private View mFragmentContainerView;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private ListView mListPlaces;
	private ArrayAdapter<CharSequence> mAdapterPlaces;
	private WeakReference<OnDrawerCallbackListener> listenerRef;

	public interface OnDrawerCallbackListener {
		void onPlaceSelected(int placeId);
		void onSettingsSelected();
		void onFeedbackSelected();
	}

	public void setup(@IdRes int drawerFragmentContainer, DrawerLayout drawerLayout, Toolbar toolbar) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean userLearnedDrawer = mPrefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		mDrawerLayout = drawerLayout;
		mFragmentContainerView = getActivity().findViewById(drawerFragmentContainer);
		mDrawerToggle = new ActionBarDrawerToggle(
						getActivity(),  mDrawerLayout, toolbar,
						R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

				ActivityCompat.invalidateOptionsMenu(getActivity());
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				ActivityCompat.invalidateOptionsMenu(getActivity());
			}
		};

		if (!userLearnedDrawer) {
			userLearnedDrawer = true;
			SharedPreferencesCompat.apply(mPrefs.edit().putBoolean(PREF_USER_LEARNED_DRAWER, userLearnedDrawer));

			openDrawer();
		}

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void setPlaceId(int placeId) {
		mListPlaces.setItemChecked(placeId, true);
		mListPlaces.setSelection(placeId);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
		prepareActions(view);

		return view;
	}

	@Override
	public void onDestroyView() {
		mFragmentContainerView = null;
		super.onDestroyView();
	}

	private void preparePlaces(@NonNull View view) {
		mAdapterPlaces = ArrayAdapter.createFromResource(getActivity(), R.array.places, R.layout.drawer_list_item);

		mListPlaces = (ListView) view.findViewById(R.id.place_list);
		mListPlaces.setAdapter(mAdapterPlaces);
		mListPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OnDrawerCallbackListener listener = listenerRef.get();
				if (listener != null) {
					listener.onPlaceSelected(position);
				}
				closeDrawer();
			}
		});
	}

	private void prepareActions(@NonNull View view) {
		List<DrawerListAdapter.Item> actions = new ArrayList<>();
		actions.add(new DrawerListAdapter.Item(getString(R.string.action_settings), null));
		actions.add(new DrawerListAdapter.Item(getString(R.string.action_feedback), null));

		DrawerListAdapter mAdapterActions = new DrawerListAdapter(getActivity(), actions);

		ListView mListActions = (ListView) view.findViewById(R.id.action_list);
		mListActions.setAdapter(mAdapterActions);
		mListActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OnDrawerCallbackListener listener = listenerRef.get();
				if (listener != null) {
					switch (position) {
						case 0:
							listener.onSettingsSelected();
							break;
						case 1:
							listener.onFeedbackSelected();
							break;
					}
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}
}
