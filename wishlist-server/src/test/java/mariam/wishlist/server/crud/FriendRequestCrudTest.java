package mariam.wishlist.server.crud;

import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.server.database.DatabaseHandler;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FriendRequestCrudTest {

    private static FriendRequestCrud friendRequestCrud;
    private static FriendCrud friendCrud;

    @BeforeAll
    public static void setUp() throws Exception {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        friendRequestCrud = new FriendRequestCrud(handler);
        friendCrud = new FriendCrud(handler);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        for (FriendRequest friendRequest : friendRequestCrud.getAllFriendRequests()) {
            friendRequestCrud.deleteFriendRequest(friendRequest.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }

        friendRequestCrud.close();
        friendCrud.close();
    }

    @BeforeEach
    public void clearFriendRequests() throws Exception {
        for (FriendRequest friendRequest : friendRequestCrud.getAllFriendRequests()) {
            friendRequestCrud.deleteFriendRequest(friendRequest.getId());
        }

        for (Friend friend : friendCrud.getAllFriends()) {
            friendCrud.deleteFriend(friend.getId());
        }
    }

    @Test
    public void should_create_friendRequest() throws Exception {
        Friend sender = friendCrud.createFriend(new Friend("john", "1"));
        Friend receiver = friendCrud.createFriend(new Friend("jane", "1"));

        FriendRequest createdFriendRequest = friendRequestCrud.createFriendRequest(new FriendRequest(sender, receiver, FriendRequest.Status.SENT));

        List<FriendRequest> allFriendRequests = friendRequestCrud.getAllFriendRequests();

        boolean idsMatch = allFriendRequests.stream().anyMatch(someFriendRequest -> someFriendRequest.getId() == createdFriendRequest.getId());

        assertTrue(idsMatch);
    }

    @Test
    public void should_get_friendRequest_by_id() throws Exception {
        Friend sender = friendCrud.createFriend(new Friend("john", "1"));
        Friend receiver = friendCrud.createFriend(new Friend("jane", "1"));

        FriendRequest createdFriendRequest = friendRequestCrud.createFriendRequest(new FriendRequest(sender, receiver, FriendRequest.Status.SENT));

        FriendRequest foundFriendRequest = friendRequestCrud.getFriendRequestById(createdFriendRequest.getId());

        assertEquals(createdFriendRequest, foundFriendRequest);
    }

    @Test
    public void should_update_an_friendRequest() throws Exception {
        Friend sender = friendCrud.createFriend(new Friend("john", "1"));
        Friend receiver = friendCrud.createFriend(new Friend("jane", "1"));

        FriendRequest createdFriendRequest = friendRequestCrud.createFriendRequest(new FriendRequest(sender, receiver, FriendRequest.Status.SENT));

        FriendRequest foundFriendRequest = friendRequestCrud.getFriendRequestById(createdFriendRequest.getId());

        foundFriendRequest = new FriendRequest(foundFriendRequest.getId(), foundFriendRequest.getSender(), foundFriendRequest.getReceiver(), FriendRequest.Status.CONFIRMED);

        friendRequestCrud.updateFriendRequest(foundFriendRequest);

        FriendRequest updatedFriendRequest = friendRequestCrud.getFriendRequestById(foundFriendRequest.getId());

        assertEquals(updatedFriendRequest, foundFriendRequest);
    }

    @Test
    public void should_delete_an_friendRequest() throws Exception {
        Friend sender = friendCrud.createFriend(new Friend("john", "1"));
        Friend receiver = friendCrud.createFriend(new Friend("jane", "1"));

        FriendRequest createdFriendRequest = friendRequestCrud.createFriendRequest(new FriendRequest(sender, receiver, FriendRequest.Status.SENT));

        FriendRequest foundFriendRequest = friendRequestCrud.getFriendRequestById(createdFriendRequest.getId());

        friendRequestCrud.deleteFriendRequest(foundFriendRequest.getId());

        FriendRequest deletedFriendRequest = friendRequestCrud.getFriendRequestById(foundFriendRequest.getId());

        assertNull(deletedFriendRequest);
    }

}
