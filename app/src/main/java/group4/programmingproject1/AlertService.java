package group4.programmingproject1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.dyndns.ecall.ecalldataapi.*;
import org.dyndns.ecall.ecallsendapi.*;

public class AlertService extends Service {
    public AlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
