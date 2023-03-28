package mariam.wishlist.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import mariam.wishlist.server.database.DatabaseHandler;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private final int port;
    private final Map<UUID, ClientHandlerTask> connections;
    private final ExecutorService executor;
    private final DatabaseHandler databaseHandler;
    private ServerSocket serverSocket;
    private boolean running;
    private Thread serverThread;

    public Server(int port, DatabaseHandler databaseHandler) throws IOException, SQLException {
        this.port = port;
        this.databaseHandler = databaseHandler;

        connections = new ConcurrentHashMap<>();
        // create a thread pool with 100 threads
        executor = Executors.newFixedThreadPool(100);
    }

    void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        LOG.log(Level.INFO, "Server started on port: {0}", port);

        serverThread = new Thread(() -> {
            // accept client connections and submit tasks to the executor
            while (running) {
                try {
                    // accept a new client connection
                    Socket clientSocket = serverSocket.accept();
                    ClientConnection connection = new ClientConnection(clientSocket);
                    // get a unique identifier for the client
                    UUID clientId = connection.getClientId();
                    ClientHandlerTask clientHandlerTask = new ClientHandlerTask(clientId, connection.getClientSocket(), this);
                    // add the client handler task to the collection
                    connections.put(clientId, clientHandlerTask);
                    // submit a new task to the executor to handle the client request
                    executor.submit(clientHandlerTask);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        });

        serverThread.start();
    }

    void stop() throws IOException {
        running = false;
        try {
            running = false;
            executor.shutdown();
            serverSocket.close();
            for (ClientHandlerTask clientHandlerTask : connections.values()) {
                clientHandlerTask.stop();
            }
            connections.clear();

            LOG.log(Level.INFO, "Server stopped");

            serverThread.interrupt();
            serverThread.join();
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    void removeClient(UUID clientId) {
        connections.remove(clientId);
    }

    boolean isRunning() {
        return running;
    }

    DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

}
