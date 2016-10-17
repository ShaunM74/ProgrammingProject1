package group4.programmingproject1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import org.dyndns.ecall.ecalldataapi.*;
import org.dyndns.ecall.ecallsendapi.*;

public class AlertService extends Service {
    public AlertService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //handleCommand(intent);
        startAlert();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void startAlert()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String nameKey = getString(R.string.pref_contact_name);
        String existingName = sharedPreferences.getString(nameKey,null);
        String emailKey = getString(R.string.pref_contact_email);
        String existingEmail = sharedPreferences.getString(emailKey,null);
        String phoneKey = getString(R.string.pref_contact_phone_number);
        String existingPhone = sharedPreferences.getString(phoneKey,null);
        String thumbKey = getString(R.string.pref_contact_image_uri);
        String existingThumb = sharedPreferences.getString(thumbKey,null);
        Toast.makeText(this, existingName, Toast.LENGTH_LONG).show();

    }
}
