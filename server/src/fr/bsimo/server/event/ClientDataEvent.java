package fr.bsimo.server.event;

import fr.bsimo.server.ServerClient;
import org.json.JSONObject;

/**
 * Created by BSimo on 30/04/16.
 */
public class ClientDataEvent extends ClientEvent {
    private byte[] data;

    public ClientDataEvent(ServerClient client, byte[] data) {
        super(client);
        this.data = data;
    }

    public final byte[] getData() {
        return this.data;
    }
    public final String getDataString() {
        return new String(this.data);
    }
    public final JSONObject getDataJSON() {
        return new JSONObject(new String(this.data));
    }
}
