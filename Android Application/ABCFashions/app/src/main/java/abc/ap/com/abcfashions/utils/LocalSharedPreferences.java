package abc.ap.com.abcfashions.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Aparna Prasad on 1/22/2017.
 */
public class LocalSharedPreferences {

	private final String STORE_KEY = "SettingsStoreKey";

	private Context context;

	public LocalSharedPreferences(Context context) {
		this.context = context;
	}

	private static LocalSharedPreferences localSharedPreferences;

	public static LocalSharedPreferences GetInstance(Context context) {
		if (localSharedPreferences == null)
			localSharedPreferences = new LocalSharedPreferences(context);

		return localSharedPreferences;
	}


	public void StoreUser(){
		try {
			SharedPreferences settings = context.getSharedPreferences(
					STORE_KEY, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();

			editor.putBoolean("stored", true);
			editor.apply();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean IsUserStored()
	{
		try {
			SharedPreferences settings = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE);
			return settings.getBoolean("stored", false);
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	public void ClearStoredUser() {
		try {
			SharedPreferences settings = context.getSharedPreferences(
					STORE_KEY, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.clear();
			editor.apply();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
