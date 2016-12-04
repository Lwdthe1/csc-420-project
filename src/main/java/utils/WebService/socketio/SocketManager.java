package utils.WebService.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class SocketManager {
    private Socket socket;
    private String host = "http://lcontacts.herokuapp.com";
    private ConcurrentHashMap<String, LinkedList<SocketListener>> eventListenersMap = new ConcurrentHashMap<>();

    public SocketManager() {
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
                System.out.println("RECEIVED NUMCLIENTS EVENT FROM SERVER:" + args[0]);
                notifyListeners(SocketEvent.NUM_CLIENTS, (JSONObject) args[0]);
            }
        }).on(SocketEvent.CHAT_MESSAGE.getValue() + "-pid-pub1", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("RECEIVED NUMCLIENTS EVENT FROM SERVER:" + args[0]);
                notifyListeners(SocketEvent.CHAT_MESSAGE, (JSONObject) args[0]);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCKET DISCONNECTED FROM:" + host);
                notifyListeners(SocketEvent.DISCONNECTED, (JSONObject) args[0]);
            }
        });
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
        LinkedList<SocketListener> eventListeners = eventListenersMap.get(event.getValue());
        if (eventListeners != null) {
            for (SocketListener listener: eventListeners) {
                listener.onEvent(event, payload);
                listener.onEvent(event.getValue(), payload);
            }
        }
    }
}
