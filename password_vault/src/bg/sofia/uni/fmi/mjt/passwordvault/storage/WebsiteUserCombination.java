package bg.sofia.uni.fmi.mjt.passwordvault.storage;

import java.util.Objects;

public record WebsiteUserCombination(String website, String user) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsiteUserCombination that = (WebsiteUserCombination) o;
        return Objects.equals(website, that.website) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(website, user);
    }
}
