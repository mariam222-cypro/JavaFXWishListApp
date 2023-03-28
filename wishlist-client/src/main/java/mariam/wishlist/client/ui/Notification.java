package mariam.wishlist.client.ui;

import java.math.BigDecimal;
import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.Gift;

public record Notification(Friend receiver, List<Friend> contributors, Gift giftItem, BigDecimal amountContributed) {


}
