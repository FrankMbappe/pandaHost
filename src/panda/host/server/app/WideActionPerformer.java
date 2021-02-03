package panda.host.server.app;

import panda.host.server.SyncChannel;
import panda.host.utils.Current;

public class WideActionPerformer {
    public static void perform(WideAction wideAction, Object valueToBeSynced){
        System.out.println("[WAPerformer, perform()] | Action started.");

        // I iterate through the registered clients to sync their valuesToBeSynced, using their SyncChannel
        System.out.println("[WAPerformer, perform()] | Number of registered clients: " + Current.registeredClients.size());
        for (var clientRegistration : Current.registeredClients.entrySet()) {
            String clientId = clientRegistration.getKey().getId();
            SyncChannel syncChannel = clientRegistration.getValue();

            wideAction.updateRemoteObject(clientId, syncChannel, valueToBeSynced);
        }

        System.out.println("[WideActionPerformer, perform()] | Action ended.");
    }
}
