package mariam.wishlist.core.model;

import java.io.Serializable;
import java.util.Objects;

public class Gift implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final Item item;
    private final Wishlist wishlist;

    public Gift(Item item, Wishlist wishlist) {
        this(-1, item, wishlist);
    }

    public Gift(int id, Item item, Wishlist wishlist) {
        this.id = id;
        this.item = item;
        this.wishlist = wishlist;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.item);
        hash = 67 * hash + Objects.hashCode(this.wishlist);
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
        final Gift other = (Gift) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        return Objects.equals(this.wishlist, other.wishlist);
    }

    @Override
    public String toString() {
        return "Gift{" + "id=" + id + ", item=" + item + ", wishlist=" + wishlist + '}';
    }

}
