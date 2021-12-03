package com.mazenetsolutions.mzs119.skst_new.Utils;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatSpinner;

import java.lang.reflect.Field;

public class NDSpinner extends AppCompatSpinner {

	public NDSpinner(Context context) {
		super(context);
	}

	public NDSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NDSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void ignoreOldSelectionByReflection() {
		try {
			Class<?> c = this.getClass().getSuperclass().getSuperclass()
					.getSuperclass();
			Field reqField = c.getDeclaredField("mOldSelectedPosition");
			reqField.setAccessible(true);
			reqField.setInt(this, -1);
		} catch (Exception e) {
			Log.d("Exception Private", "ex", e);
			// TODO: handle exception
		}
	}

	@Override
	public void setSelection(int position, boolean animate) {
		boolean sameSelected = position == getSelectedItemPosition();
		ignoreOldSelectionByReflection();
		super.setSelection(position, animate);
		if (sameSelected) {

			getOnItemSelectedListener().onItemSelected(this, getSelectedView(),
					position, getSelectedItemId());

		}
	}

	@Override
	public void setSelection(int position) {
		ignoreOldSelectionByReflection();
		super.setSelection(position);
	}

}