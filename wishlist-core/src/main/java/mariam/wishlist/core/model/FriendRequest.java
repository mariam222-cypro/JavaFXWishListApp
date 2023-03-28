package mariam.wishlist.core.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FriendRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int id;
    private final Friend sender;
    private final Friend receiver;
    private final Status status;

    public enum Status {
        SENT, DECLINED, CONFIRMED;
        
        public static Status from(String text){
            return Arrays.stream(values())
                    .filter(s -> s.name().equals(text))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public FriendRequest(Friend sender, Friend receiver, Status status) {
        this(-1, sender, receiver, status);
    }

    public FriendRequest(int id, Friend sender, Friend receiver, Status status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Friend getSender() {
        return sender;
    }

    public Friend getReceiver() {
        return receiver;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.id;
        hash = 47 * hash + Objects.hashCode(this.sender);
        hash = 47 * hash + Objects.hashCode(this.receiver);
        hash = 47 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FriendRequest other = (FriendRequest) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        return this.status == other.status;
    }

    @Override
    public String toString() {
        return "FriendRequest{" + "id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", status=" + status + '}';
    }

}
