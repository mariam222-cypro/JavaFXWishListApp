package mariam.wishlist.server;

import java.net.Socket;
import java.util.UUID;

class ClientConnection {

    private final UUID clientId;
    private final Socket clientSocket;

    ClientConnection(Socket clientSocket) {
        this.clientId = UUID.randomUUID();
        this.clientSocket = clientSocket;
    }

    UUID getClientId() {
        return clientId;
    }

    Socket getClientSocket() {
        return clientSocket;
    }
}
