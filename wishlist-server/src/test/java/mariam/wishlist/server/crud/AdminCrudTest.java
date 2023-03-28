package mariam.wishlist.server.crud;

import java.util.List;
import mariam.wishlist.core.model.Admin;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class AdminCrudTest {

    private static AdminCrud adminCRUD;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        adminCRUD = new AdminCrud(handler);
    }

    @BeforeEach
    public void truncateAdminTable() throws Exception {
        List<Integer> ids = adminCRUD.getAllAdmins().stream().map(Admin::getId).toList();

        for (Integer id : ids) {
            adminCRUD.deleteAdmin(id);
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        adminCRUD.close();
    }

    @Test
    public void should_get_all_admins() throws Exception {
        List<Admin> admins = adminCRUD.getAllAdmins();

        assertTrue(admins.isEmpty());
    }

    @Test
    public void should_create_an_admin() throws Exception {
        Admin newAdmin = adminCRUD.createAdmin(new Admin("johndoe", "password"));

        assertNotEquals(newAdmin.getId(), 0);
    }

    @Test
    public void should_get_an_admin_by_id() throws Exception {
        Admin newAdmin = adminCRUD.createAdmin(new Admin("johndoe", "password"));

        Admin gotAdmin = adminCRUD.getAdminById(newAdmin.getId());

        Assertions.assertEquals(newAdmin, gotAdmin);
    }

    @Test
    public void should_update_an_admin() throws Exception {
        Admin admin = adminCRUD.createAdmin(new Admin("johndoe", "password"));

        adminCRUD.updateAdmin(new Admin(admin.getId(), "janedoe", "hidden"));

        Admin gotAdmin = adminCRUD.getAdminById(admin.getId());

        assertNotEquals(admin, gotAdmin);
    }

    @Test
    public void should_delete_an_admin() throws Exception {
        Admin admin = adminCRUD.createAdmin(new Admin("johndoe", "password"));

        adminCRUD.deleteAdmin(admin.getId());

        assertNull(adminCRUD.getAdminById(admin.getId()));
    }
}
