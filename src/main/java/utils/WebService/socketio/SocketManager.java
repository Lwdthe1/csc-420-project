package utils.WebService.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class SocketManager {
    public static SocketManager sharedInstance = new SocketManager();
    private Socket socket;
    private String host = "http://lcontacts.herokuapp.com";
    private ConcurrentHashMap<String, LinkedList<SocketListener>> eventListenersMap = new ConcurrentHashMap<>();
    private Semaphore notifyListenersLock = new Semaphore(1);

    private SocketManager() {
    }

    public void setupAndConnect() {
        try {
            socket = IO.socket(host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        registerEventListeners();
        socket.connect();
    }
    private void registerEventListeners() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("foo", "hi");
                notifyListeners(SocketEvent.CONNECTED, new JSONObject());
            }
        }).on(SocketEvent.NUM_CLIENTS.getValue(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                normalizeIntPayload(args[0]);
                System.out.println("RECEIVED NUM_CLIENTS EVENT FROM SERVER:" + args[0]);
                notifyListeners(SocketEvent.NUM_CLIENTS, (JSONObject) args[0]);
            }
        }).on(SocketEvent.CHAT_MESSAGE.getValue(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("RECEIVED CHAT_MESSAGE EVENT FROM SERVER:" + args[0]);
                notifyListeners(SocketEvent.CHAT_MESSAGE, (JSONObject) args[0]);
            }
        }).on(SocketEvent.NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION.getValue(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("RECEIVED NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION EVENT FROM SERVER:" + args[0]);
                notifyListeners(SocketEvent.NOTIFICATION_REQUEST_TO_CONTRIBUTE_DECISION, (JSONObject) args[0]);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //normalizeStringPayload(args[0]);
                System.out.println("SOCKET DISCONNECTED FROM:" + args[0]);
                //notifyListeners(SocketEvent.DISCONNECTED, (JSONObject) args[0]);
            }
        });
    }

    private void normalizeStringPayload(Object arg) {
        String currentPayload = (String) ((JSONObject) arg).get("payload");
        JSONObject newPayload = new JSONObject();
        newPayload.put("value", currentPayload);
        ((JSONObject) arg).put("payload", newPayload);
    }

    private void normalizeIntPayload(Object arg) {
        int currentPayload = (int) ((JSONObject) arg).get("payload");
        JSONObject newPayload = new JSONObject();
        newPayload.put("value", currentPayload);
        ((JSONObject) arg).put("payload", newPayload);
    }


    public void emit(SocketEvent event, JSONObject obj) {
        socket.emit(event.getValue(), obj);
        System.out.printf("\nSocketManager[emit] >> Sent event: %s\n", event.getValue());
    }

    public void listen(SocketEvent event, SocketListener listener) {
        eventListenersMap.putIfAbsent(event.getValue(), new LinkedList<SocketListener>());
        eventListenersMap.get(event.getValue()).add(listener);
    }

    private void notifyListeners(SocketEvent event, JSONObject obj) {
        JSONObject payload = obj.has("payload") && obj.get("payload") instanceof JSONObject ? (JSONObject) obj.get("payload") : null;
        try {
            notifyListenersLock.acquire();
            LinkedList<SocketListener> eventListeners = (LinkedList<SocketListener>) eventListenersMap.get(event.getValue()).clone();
            if (eventListeners != null) {
                for (SocketListener listener: eventListeners) {
                    listener.onEvent(event, payload);
                }
            }
            notifyListenersLock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
