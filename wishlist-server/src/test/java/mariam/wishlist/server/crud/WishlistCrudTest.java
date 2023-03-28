package mariam.wishlist.server.crud;

import java.math.BigDecimal;
import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WishlistCrudTest {

    private static WishlistCrud wishListCrud;
    private static FriendCrud friendCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        wishListCrud = new WishlistCrud(handler);
        friendCrud = new FriendCrud(handler);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        List<Wishlist> wishLists = wishListCrud.getAllWishlists();

        for (Wishlist wishList : wishLists) {
            wishListCrud.deleteWishlist(wishList.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }
        
        wishListCrud.close();
        friendCrud.close();
    }

    @BeforeEach
    public void clearWishLists() throws Exception {
        List<Wishlist> wishLists = wishListCrud.getAllWishlists();

        for (Wishlist wishList : wishLists) {
            wishListCrud.deleteWishlist(wishList.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }
    }

    @Test
    public void should_create_wishList() throws Exception {
        Friend owner = friendCrud.createFriend(new Friend("john", "1"));

        Wishlist createdWishList = wishListCrud.createWishlist(new Wishlist(owner, "mine"));

        List<Wishlist> allWishLists = wishListCrud.getAllWishlists();

        boolean idsMatch = allWishLists.stream().anyMatch(someWishList -> someWishList.getId() == createdWishList.getId());

        assertTrue(idsMatch);
    }

    @Test
    public void should_get_wishList_by_id() throws Exception {
        Friend owner = friendCrud.createFriend(new Friend("john", "1"));

        Wishlist createdWishList = wishListCrud.createWishlist(new Wishlist(owner, "mine"));

        Wishlist foundWishList = wishListCrud.getWishlistById(createdWishList.getId());

        assertEquals(createdWishList, foundWishList);
    }

    @Test
    public void should_update_an_wishList() throws Exception {
        Friend owner = friendCrud.createFriend(new Friend("john", "1"));

        Wishlist createdWishList = wishListCrud.createWishlist(new Wishlist(owner, "mine"));

        Wishlist foundWishList = wishListCrud.getWishlistById(createdWishList.getId());

        foundWishList = new Wishlist(foundWishList.getId(), owner, "yours", null);

        wishListCrud.updateWishlist(foundWishList);

        Wishlist updatedWishList = wishListCrud.getWishlistById(foundWishList.getId());

        assertEquals(updatedWishList, foundWishList);
    }

    @Test
    public void should_delete_an_wishList() throws Exception {
        Friend owner = friendCrud.createFriend(new Friend("john", "1"));

        Wishlist createdWishList = wishListCrud.createWishlist(new Wishlist(owner, "mine"));

        Wishlist foundWishList = wishListCrud.getWishlistById(createdWishList.getId());

        wishListCrud.deleteWishlist(foundWishList.getId());

        Wishlist deletedWishList = wishListCrud.getWishlistById(foundWishList.getId());

        assertNull(deletedWishList);
    }

}
