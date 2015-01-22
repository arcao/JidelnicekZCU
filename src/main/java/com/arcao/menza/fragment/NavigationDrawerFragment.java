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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.arcao.menza.R;
import com.arcao.menza.adapter.DrawerRecyclerAdapter;
import com.arcao.menza.util.SharedPreferencesCompat;
import com.arcao.menza.util.ThemedDrawable;
import com.arcao.menza.widget.WrapContentLinearLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {
	private static final String TAG = "NavigationDrawerFragment";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private TextView placeTextView;
	private View mFragmentContainerView;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerRecyclerAdapter mAdapterPlaces;
	private WeakReference<OnDrawerCallbackListener> listenerRef;

	public interface OnDrawerCallbackListener {
		void onPlaceSelected(int placeId);
		void onSettingsSelected();
		void onFeedbackSelected();
	}

	public void setup(@IdRes int drawerFragmentContainer, DrawerLayout drawerLayout, Toolbar toolbar) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
		mAdapterPlaces.setSelected(placeId);
		placeTextView.setText(mAdapterPlaces.getItem(placeId).name);
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
		View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

		placeTextView = (TextView) view.findViewById(R.id.title);

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
		List<DrawerRecyclerAdapter.Item> items = new ArrayList<>();

		for (String place : getResources().getStringArray(R.array.places)) {
			items.add(new DrawerRecyclerAdapter.Item(place, null));
		}

		mAdapterPlaces = new DrawerRecyclerAdapter(items);

		RecyclerView listPlaces = (RecyclerView) view.findViewById(R.id.place_list);
		listPlaces.setAdapter(mAdapterPlaces);
		listPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
		listPlaces.setHasFixedSize(true);

		mAdapterPlaces.setSelectable(true);
		mAdapterPlaces.setOnItemClickListener(new DrawerRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.Adapter<?> parent, View view, int position) {
				placeTextView.setText(mAdapterPlaces.getItem(position).name);

				OnDrawerCallbackListener listener = listenerRef.get();
				if (listener != null) {
					listener.onPlaceSelected(position);
				}
				closeDrawer();
			}
		});
	}

	private void prepareActions(@NonNull View view) {
		List<DrawerRecyclerAdapter.Item> actions = new ArrayList<>();
		actions.add(new DrawerRecyclerAdapter.Item(getString(R.string.action_settings), ThemedDrawable.getDrawable(getActivity(), R.drawable.ic_drawer_settings)));
		actions.add(new DrawerRecyclerAdapter.Item(getString(R.string.action_feedback), ThemedDrawable.getDrawable(getActivity(), R.drawable.ic_drawer_question_answer)));

		DrawerRecyclerAdapter mAdapterActions = new DrawerRecyclerAdapter(actions);

		RecyclerView listActions = (RecyclerView) view.findViewById(R.id.action_list);
		listActions.setAdapter(mAdapterActions);
		listActions.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
		listActions.setHasFixedSize(true);

		mAdapterActions.setOnItemClickListener(new DrawerRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.Adapter<?> parent, View view, int position) {
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
		return mAdapterPlaces.getItem(placeId).name;
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
