package johnwilde.androidchessclock;

import johnwilde.androidchessclock.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**  Activity that inflates the preferences from XML.
 * 
 * It also updates the view with the current preference value
 * and reports back which prefs were changed. 
 */
public class TimerOptions extends PreferenceActivity
  implements OnSharedPreferenceChangeListener {
	
	// This enum stores the preference keys 
	// that are defined in preferences.xml
	public enum Key{
		MINUTES("initial_minutes_preference"),
		SECONDS("initial_seconds_preference"),
		INCREMENT_SECONDS("increment_preference"),
		NEGATIVE_TIME("allow_negative_time_preference"),
		SCREEN_DIM("screen_dim_preference");
		private String mValue;

		public String toString(){
			return mValue;
		}
		
		Key(String value){
			mValue = value;
		}
	}
	
	// This enum is used to tag boolean flags in  the
	// Intent that is returned by this activity.  The ChessTimer
	// can then take the appropriate action.
	public enum TimerPref{
		TIME("johnwilde.androidchessclock.NewTime"),			// user changed the initial time 
		INCREMENT("johnwilde.androidchessclock.NewIncrement"),  // user changed the increment field
		NEGATIVE_TIME("johnwilde.androidchessclock.NegativeTime"),  // user changed the increment field
		SCREEN("johnwilde.androidchessclock.NewScreenDim"); 	// user changed the screen dim option
		private String mValue;

		public String toString(){
			return mValue;
		}
		TimerPref(String value){
			mValue = value;
		}
	}	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, 
    		String key) {
    	Preference pref = findPreference(key);

        if (key.equals(Key.MINUTES.toString() ) ||
        	key.equals(Key.SECONDS.toString() ) )
        	setResult(RESULT_OK, getIntent().putExtra(TimerPref.TIME.toString(), true));
        
        if (key.equals(Key.INCREMENT_SECONDS.toString() ) )
        	setResult(RESULT_OK, getIntent().putExtra(TimerPref.INCREMENT.toString(), true));

        if (key.equals(Key.NEGATIVE_TIME.toString() ) )
        	setResult(RESULT_OK, getIntent().putExtra(TimerPref.NEGATIVE_TIME.toString(), true));
        
        if (key.equals(Key.SCREEN_DIM.toString()) )
        	setResult(RESULT_OK, getIntent().putExtra(TimerPref.SCREEN.toString(), true));
        
        if (pref instanceof EditTextPreference) {
        	EditTextPreference editTextPref = (EditTextPreference) pref;
        	String s = editTextPref.getText();
        	if (s.trim().length() == 0)
        		editTextPref.setText("0");
        	editTextPref.setSummary("Current value is: " + editTextPref.getText());
        }
    }    
    
    @Override
    protected void onResume() {
        super.onResume();
        // Setup the initial values
        for (Key k : Key.values()){
        	Preference p = findPreference(k.toString());
        	if (p instanceof EditTextPreference){
        		EditTextPreference pref = (EditTextPreference)p;
        		pref.setSummary("Current value is: " + pref.getText());
        	}
        }
        
        // Set up a listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }
    
}
