package mariam.wishlist.server.crud;

import java.math.BigDecimal;
import java.util.List;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemCrudTest {

    private static ItemCrud itemCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        itemCrud = new ItemCrud(handler);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        itemCrud.close();
    }

    @BeforeEach
    public void clearItems() throws Exception {
        List<Item> items = itemCrud.getAllItems();

        for (Item item : items) {
            itemCrud.deleteItem(item.getId());
        }
    }

    @Test
    public void should_create_item() throws Exception {
        Item createdItem = itemCrud.createItem(new Item(-1, "something", "abcd", BigDecimal.ONE));

        List<Item> allItems = itemCrud.getAllItems();

        boolean idsMatch = allItems.stream().anyMatch(someItem -> someItem.getId() == createdItem.getId());

        assertTrue(idsMatch);
    }

    @Test
    public void should_get_item_by_id() throws Exception {
        Item createdItem = itemCrud.createItem(new Item(-1, "something", "abcd", BigDecimal.valueOf(1L, 2)));

        Item foundItem = itemCrud.getItemById(createdItem.getId());

        assertEquals(createdItem, foundItem);
    }

    @Test
    public void should_update_an_item() throws Exception {
        Item createdItem = itemCrud.createItem(new Item(-1, "something", "abcd", BigDecimal.valueOf(1L, 2)));

        Item foundItem = itemCrud.getItemById(createdItem.getId());

        foundItem = new Item(foundItem.getId(), "else", foundItem.getDescription(), foundItem.getCost());

        itemCrud.updateItem(foundItem);

        Item updatedItem = itemCrud.getItemById(foundItem.getId());

        assertEquals(updatedItem, foundItem);
    }

    @Test
    public void should_delete_an_item() throws Exception {
        Item createdItem = itemCrud.createItem(new Item(-1, "something", "abcd", BigDecimal.valueOf(1L, 2)));

        Item foundItem = itemCrud.getItemById(createdItem.getId());

        itemCrud.deleteItem(foundItem.getId());

        Item deletedItem = itemCrud.getItemById(foundItem.getId());

        assertNull(deletedItem);
    }

}
