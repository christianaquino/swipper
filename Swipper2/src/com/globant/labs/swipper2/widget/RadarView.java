package com.globant.labs.swipper2.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.globant.labs.swipper2.MonocleActivity;
import com.globant.labs.swipper2.R;
import com.globant.labs.swipper2.models.Place;
import com.globant.labs.swipper2.utils.DroidUtils;
import com.globant.labs.swipper2.utils.GeometryUtils;
import com.google.android.gms.maps.model.LatLngBounds;

public class RadarView extends MonocleComponentViewGroup {

	private static final int RADAR_POINTS_SIZE_DP = 8;
	private static int RADAR_POINTS_SIZE_PX;
	private static int RADAR_POINTS_SIZE_PX_HALF;

	private LayoutParams mLayoutParams;

	public RadarView(Context context) {
		this(context, null);
	}

	public RadarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RadarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		RADAR_POINTS_SIZE_PX = DroidUtils.dpToPx(RADAR_POINTS_SIZE_DP, getContext());
		RADAR_POINTS_SIZE_PX_HALF = RADAR_POINTS_SIZE_PX / 2;

		mLayoutParams = new LayoutParams(RADAR_POINTS_SIZE_PX, RADAR_POINTS_SIZE_PX);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addPlaceView(Place place) {
		View placeView = new View(getContext());
		placeView.setLayoutParams(mLayoutParams);
		// cannot use switch e.e
		if (place.getCategory().equals(getLodgingString())) {
			placeView.setBackgroundDrawable(getLodgingDrawable());
		} else if (place.getCategory().equals(getTaxiString())) {
			placeView.setBackgroundDrawable(getTaxiDrawable());
		} else if (place.getCategory().equals(getGasString())) {
			placeView.setBackgroundDrawable(getGasDrawable());
		} else if (place.getCategory().equals(getCarRentalString())) {
			placeView.setBackgroundDrawable(getCarRentalDrawable());
		} else if (place.getCategory().equals(getFoodString())) {
			placeView.setBackgroundDrawable(getFoodDrawable());
		} else {
			Toast.makeText(getContext(), "da fuq?", Toast.LENGTH_SHORT).show();
		}
		placeView.setTag(place.getId());
		addView(placeView);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// In onLayout you need to call layout method on each child of this
		// ViewGroup and provide desired position (relatively to parent) for
		// them. You can check source code of FrameLayout (one of the simpliest
		// subclasses of ViewGroup) to find out how it works.

		if (getActivity().getCurrentLocation() != null) {
			LatLngBounds latLngBounds = getActivity()
					.getBounds(2 * MonocleActivity.BASE_COEFICIENT);
			int size_x = right - left;
			int size_y = bottom - top;
			for (int i = 0; i < getChildCount(); i++) {
				View v = getChildAt(i);
				String placeId = (String) v.getTag();
				Point point = GeometryUtils.locationToRadarPoint(getPlaces().get(placeId),
						latLngBounds, size_x, size_y, getActivity().getAzimuthDegrees());
				v.layout(point.x - RADAR_POINTS_SIZE_PX_HALF, point.y - RADAR_POINTS_SIZE_PX_HALF,
						point.x + RADAR_POINTS_SIZE_PX_HALF, point.y + RADAR_POINTS_SIZE_PX_HALF);
			}
		}
	}

	@Override
	protected void setUpBackgroundDrawables() {
		setLodgingDrawable(getResources().getDrawable(R.drawable.radar_item_lodging));
		setTaxiDrawable(getResources().getDrawable(R.drawable.radar_item_taxi));
		setGasDrawable(getResources().getDrawable(R.drawable.radar_item_gas));
		setCarRentalDrawable(getResources().getDrawable(R.drawable.radar_item_car_rental));
		setFoodDrawable(getResources().getDrawable(R.drawable.radar_item_food));
	}
}
