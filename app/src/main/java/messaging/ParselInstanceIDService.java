package messaging;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vaibhav.rustogi on 2/24/17.
 */

public class ParselInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
    }
}
