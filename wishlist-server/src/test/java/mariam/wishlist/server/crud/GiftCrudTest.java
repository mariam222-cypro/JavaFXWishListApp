package mariam.wishlist.server.crud;

import java.math.BigDecimal;
import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GiftCrudTest {

    private static GiftCrud giftCrud;
    private static WishlistCrud wishlistCrud;
    private static ItemCrud itemCrud;
    private static FriendCrud friendCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        giftCrud = new GiftCrud(handler);
        wishlistCrud = new WishlistCrud(handler);
        itemCrud = new ItemCrud(handler);
        friendCrud = new FriendCrud(handler);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        for (Gift gift : giftCrud.getAllGifts()) {
            giftCrud.deleteGift(gift.getId());
        }

        for (Item item : itemCrud.getAllItems()) {
            itemCrud.deleteItem(item.getId());
        }

        for (Wishlist wishlist : wishlistCrud.getAllWishlists()) {
            wishlistCrud.deleteWishlist(wishlist.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }

        giftCrud.close();
        itemCrud.close();
        wishlistCrud.close();
        friendCrud.close();
    }

    @BeforeEach
    public void clearGifts() throws Exception {
        for (Gift gift : giftCrud.getAllGifts()) {
            giftCrud.deleteGift(gift.getId());
        }

        for (Item item : itemCrud.getAllItems()) {
            itemCrud.deleteItem(item.getId());
        }

        for (Wishlist wishlist : wishlistCrud.getAllWishlists()) {
            wishlistCrud.deleteWishlist(wishlist.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }
    }

    @Test
    public void should_create_gift() throws Exception {
        Item item = itemCrud.createItem(new Item("123", "456", BigDecimal.valueOf(500L, 2)));
        Friend owner = friendCrud.createFriend(new Friend("Jane", "xxx"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "QQQ"));
        Gift createdGift = giftCrud.createGift(new Gift(item, wishlist));

        List<Gift> allGifts = giftCrud.getAllGifts();

        boolean idsMatch = allGifts.stream().anyMatch(someGift -> someGift.getId() == createdGift.getId());

        assertTrue(idsMatch);
    }

    @Test
    public void should_get_gift_by_id() throws Exception {
        Item item = itemCrud.createItem(new Item("123", "456", BigDecimal.valueOf(500L, 2)));
        Friend owner = friendCrud.createFriend(new Friend("Jane", "xxx"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "QQQ"));
        Gift createdGift = giftCrud.createGift(new Gift(item, wishlist));

        Gift foundGift = giftCrud.getGiftById(createdGift.getId());

        assertEquals(createdGift, foundGift);
    }

    @Test
    public void should_update_a_gift() throws Exception {
        Item item = itemCrud.createItem(new Item("123", "456", BigDecimal.valueOf(500L, 2)));
        Item anotherItem = itemCrud.createItem(new Item("123", "963", BigDecimal.valueOf(500L, 2)));
        Friend owner = friendCrud.createFriend(new Friend("Jane", "xxx"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "QQQ"));
        Gift createdGift = giftCrud.createGift(new Gift(item, wishlist));

        Gift foundGift = giftCrud.getGiftById(createdGift.getId());

        foundGift = new Gift(foundGift.getId(), anotherItem, wishlist);

        giftCrud.updateGift(foundGift);

        Gift updatedGift = giftCrud.getGiftById(foundGift.getId());

        assertEquals(updatedGift, foundGift);
    }

    @Test
    public void should_delete_a_gift() throws Exception {
        Item item = itemCrud.createItem(new Item("123", "456", BigDecimal.valueOf(500L, 2)));
        Friend owner = friendCrud.createFriend(new Friend("Jane", "xxx"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "QQQ"));
        Gift createdGift = giftCrud.createGift(new Gift(item, wishlist));

        giftCrud.deleteGift(createdGift.getId());

        Gift deletedGift = giftCrud.getGiftById(createdGift.getId());

        assertNull(deletedGift);
    }
}
