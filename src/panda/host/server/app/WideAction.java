package panda.host.server.app;

import panda.host.server.SyncChannel;

/**
 * An action performed to all registered clients, using their SyncChannels
 */
public interface WideAction {
    /**
     * Updating a remote object using a SyncChannel
     * @param clientId representing the Id of the client using the SyncChannel
     * @param syncChannel representing the channel used to synchronize a remote object
     * @param update representing the remote object to synchronize
     */
    void updateRemoteObject(String clientId, SyncChannel syncChannel, Object update);
}
