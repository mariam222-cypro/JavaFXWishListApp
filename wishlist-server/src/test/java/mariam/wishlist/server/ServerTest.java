package mariam.wishlist.server;

import java.io.IOException;
import java.sql.SQLException;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServerTest {

    private static final int PORT = 8080;
    private static Server server;

    @BeforeAll
    public static void setUp() throws IOException, SQLException {
        server = new Server(PORT, createDatabaseHandler());
        server.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        server.stop();
    }

    @Test
    public void testServerStart() {
        assertTrue(server.isRunning());
    }

    @Test
    public void testServerStop() throws IOException {
        server.stop();
        assertFalse(server.isRunning());
    }

    private static DatabaseHandler createDatabaseHandler() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        return handler;
    }
}
