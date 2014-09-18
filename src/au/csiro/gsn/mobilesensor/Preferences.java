package au.csiro.gsn.mobilesensor;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import au.csiro.openiot.R;

public class Preferences extends PreferenceActivity {
	private String Tag = Preferences.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}

	
}
