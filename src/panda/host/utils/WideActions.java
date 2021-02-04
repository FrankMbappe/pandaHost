package panda.host.utils;

import panda.host.server.app.WideAction;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class WideActions {
    public static WideAction ServerStatusUpdate = (clientId, syncChannel, serverStatus) -> {
        try {
            syncChannel.updateServerStatus((Boolean) serverStatus);
            System.out.println(String.format("[CHANNEL@SYNC] | %s | Channel '%s' has been synced with the server status.",
                    Panda.getFormattedDate(LocalDateTime.now()), clientId));

        } catch (RemoteException e) {
            System.err.println(String.format("[CHANNEL@SYNC] | %s | Channel '%s' is not reachable. The sync has failed.",
                    Panda.getFormattedDate(LocalDateTime.now()), clientId));
            //e.printStackTrace();
        }
    };

    public static WideAction PostListUpdate = (clientId, syncChannel, postToString) -> {
        try {
            syncChannel.updatePosts((String) postToString);
            System.out.println(String.format("[CHANNEL@SYNC] | %s > Channel '%s' has been synced with the post list.",
                    Panda.getFormattedDate(LocalDateTime.now()), clientId));

        } catch (RemoteException e) {
            System.err.println(String.format("[CHANNEL@SYNC] | %s > Channel '%s' failed to sync the post list.",
                    Panda.getFormattedDate(LocalDateTime.now()), clientId));
            e.printStackTrace();
        }
    };


}
