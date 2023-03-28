package mariam.wishlist.core.model;

import java.io.Serializable;
import java.util.Objects;

public class Wishlist implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final Friend owner;
    private final String name;
    private final String description;

    public Wishlist(Friend owner, String name) {
        this(-1, owner, name, null);
    }

    public Wishlist(Friend owner, String name, String description) {
        this(-1, owner, name, description);
    }

    public Wishlist(int id, Friend owner, String name, String description) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Friend getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.owner);
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.description);
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
        final Wishlist other = (Wishlist) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return Objects.equals(this.owner, other.owner);
    }

    @Override
    public String toString() {
        return "Wishlist{" + "id=" + id + ", owner=" + owner + ", name=" + name + ", description=" + description + '}';
    }

}
