package com.kmx.andr.lib;

import kumars.and.lib.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LibStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lib_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lib_start, menu);
		return true;
	}

}
