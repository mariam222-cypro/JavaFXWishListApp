package mariam.wishlist.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Contribution implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int id;
    private final Gift gift;
    private final Friend contributor;
    private final BigDecimal amount;

    public Contribution(Gift gift, Friend contributor) {
        this(gift, contributor, BigDecimal.ZERO);
    }

    public Contribution(Gift gift, Friend contributor, BigDecimal amount) {
        this(-1, gift, contributor, amount);
    }

    public Contribution(int id, Gift gift, Friend contributor, BigDecimal amount) {
        this.id = id;
        this.gift = gift;
        this.contributor = contributor;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Gift getGift() {
        return gift;
    }

    public Friend getContributor() {
        return contributor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        hash = 59 * hash + Objects.hashCode(this.gift);
        hash = 59 * hash + Objects.hashCode(this.contributor);
        hash = 59 * hash + Objects.hashCode(this.amount);
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
        final Contribution other = (Contribution) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.gift, other.gift)) {
            return false;
        }
        if (!Objects.equals(this.contributor, other.contributor)) {
            return false;
        }
        return Objects.equals(this.amount, other.amount);
    }

    @Override
    public String toString() {
        return "Contribution{" + "id=" + id + ", gift=" + gift + ", contributor=" + contributor + ", amount=" + amount + '}';
    }

}
