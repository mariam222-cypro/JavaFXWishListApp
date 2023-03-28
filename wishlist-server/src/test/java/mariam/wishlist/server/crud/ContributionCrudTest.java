package mariam.wishlist.server.crud;

import java.math.BigDecimal;
import java.util.List;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContributionCrudTest {

    private static ContributionCrud contributionCrud;
    private static FriendCrud friendCrud;
    private static GiftCrud giftCrud;
    private static ItemCrud itemCrud;
    private static WishlistCrud wishlistCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        contributionCrud = new ContributionCrud(handler);
        friendCrud = new FriendCrud(handler);
        giftCrud = new GiftCrud(handler);
        itemCrud = new ItemCrud(handler);
        wishlistCrud = new WishlistCrud(handler);
    }

    @BeforeEach
    public void truncateContributionTable() throws Exception {
        for (Contribution contribution : contributionCrud.getAllContributions()) {
            contributionCrud.deleteContribution(contribution.getId());
        }

        for (Gift gift : giftCrud.getAllGifts()) {
            giftCrud.deleteGift(gift.getId());
        }

        for (Wishlist wishlist : wishlistCrud.getAllWishlists()) {
            wishlistCrud.deleteWishlist(wishlist.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        for (Contribution contribution : contributionCrud.getAllContributions()) {
            contributionCrud.deleteContribution(contribution.getId());
        }

        for (Gift gift : giftCrud.getAllGifts()) {
            giftCrud.deleteGift(gift.getId());
        }

        for (Wishlist wishlist : wishlistCrud.getAllWishlists()) {
            wishlistCrud.deleteWishlist(wishlist.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }

        contributionCrud.close();
    }

    @Test
    public void should_create_a_contribution() throws Exception {
        Friend contributor = friendCrud.createFriend(new Friend("john", "123"));
        Friend owner = friendCrud.createFriend(new Friend("jane", "345"));
        Item item = itemCrud.createItem(new Item("some", "thing"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "clothes"));
        Gift gift = giftCrud.createGift(new Gift(item, wishlist));
        Contribution newContribution = contributionCrud.createContribution(new Contribution(gift, contributor));

        assertNotEquals(newContribution.getId(), 0);
    }

    @Test
    public void should_get_a_contribution_by_id() throws Exception {
        Friend contributor = friendCrud.createFriend(new Friend("john", "123"));
        Friend owner = friendCrud.createFriend(new Friend("jane", "345"));
        Item item = itemCrud.createItem(new Item("some", "thing", BigDecimal.valueOf(0L, 2)));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "clothes"));
        Gift gift = giftCrud.createGift(new Gift(item, wishlist));
        Contribution newContribution = contributionCrud.createContribution(new Contribution(gift, contributor, BigDecimal.valueOf(0L, 2)));

        Contribution gotContribution = contributionCrud.getContributionById(newContribution.getId());

        Assertions.assertEquals(newContribution, gotContribution);
    }

    @Test
    public void should_update_a_contribution() throws Exception {
        Friend contributor = friendCrud.createFriend(new Friend("john", "123"));
        Friend owner = friendCrud.createFriend(new Friend("jane", "345"));
        Item item = itemCrud.createItem(new Item("some", "thing"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "clothes"));
        Gift gift = giftCrud.createGift(new Gift(item, wishlist));
        Contribution newContribution = contributionCrud.createContribution(new Contribution(gift, contributor));

        contributionCrud.updateContribution(new Contribution(newContribution.getId(), gift, owner, BigDecimal.valueOf(1L, 2)));

        Contribution gotContribution = contributionCrud.getContributionById(newContribution.getId());

        assertNotEquals(newContribution, gotContribution);
    }

    @Test
    public void should_delete_a_contribution() throws Exception {
        Friend contributor = friendCrud.createFriend(new Friend("john", "123"));
        Friend owner = friendCrud.createFriend(new Friend("jane", "345"));
        Item item = itemCrud.createItem(new Item("some", "thing"));
        Wishlist wishlist = wishlistCrud.createWishlist(new Wishlist(owner, "clothes"));
        Gift gift = giftCrud.createGift(new Gift(item, wishlist));
        Contribution newContribution = contributionCrud.createContribution(new Contribution(gift, contributor));

        contributionCrud.deleteContribution(newContribution.getId());

        assertNull(contributionCrud.getContributionById(newContribution.getId()));
    }
}
