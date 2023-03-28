package mariam.wishlist.server.crud;

import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class FriendCrudTest {

    private static FriendCrud friendCrud;
    private static FriendRequestCrud friendRequestCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        friendCrud = new FriendCrud(handler);
        friendRequestCrud = new FriendRequestCrud(handler);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        friendCrud.close();
    }

    @BeforeEach
    public void clearFriends() throws Exception {
        for (FriendRequest friendRequest : friendRequestCrud.getAllFriendRequests()) {
            friendRequestCrud.deleteFriendRequest(friendRequest.getId());
        }
        
        List<Friend> friends = friendCrud.getAllFriends();

        for (Friend friend : friends) {
            friendCrud.deleteFriend(friend.getId());
        }
    }

    @Test
    public void should_create_friend() throws Exception {
        Friend createdFriend = friendCrud.createFriend(new Friend(-1, "something", "abcd"));

        List<Friend> allFriends = friendCrud.getAllFriends();

        boolean idsMatch = allFriends.stream().anyMatch(someFriend -> someFriend.getId() == createdFriend.getId());

        assertTrue(idsMatch);
    }

    @Test
    public void should_get_friend_by_id() throws Exception {
        Friend createdFriend = friendCrud.createFriend(new Friend(-1, "something", "abcd"));

        Friend foundFriend = friendCrud.getFriendById(createdFriend.getId());

        assertEquals(createdFriend, foundFriend);
    }

    @Test
    public void should_update_a_friend() throws Exception {
        Friend createdFriend = friendCrud.createFriend(new Friend(-1, "something", "abcd"));

        Friend foundFriend = friendCrud.getFriendById(createdFriend.getId());

        foundFriend = new Friend(foundFriend.getId(), "else", foundFriend.getPassword());

        friendCrud.updateFriend(foundFriend);

        Friend updatedFriend = friendCrud.getFriendById(foundFriend.getId());

        assertEquals(updatedFriend, foundFriend);
    }

    @Test
    public void should_delete_a_friend() throws Exception {
        Friend createdFriend = friendCrud.createFriend(new Friend(-1, "something", "abcd"));

        Friend foundFriend = friendCrud.getFriendById(createdFriend.getId());

        friendCrud.deleteFriend(foundFriend.getId());

        Friend deletedFriend = friendCrud.getFriendById(foundFriend.getId());

        assertNull(deletedFriend);
    }

}
