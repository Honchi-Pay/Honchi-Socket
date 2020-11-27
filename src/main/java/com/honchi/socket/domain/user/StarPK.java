package com.honchi.socket.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class StarPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer targetId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarPK starPK = (StarPK) o;
        return Objects.equals(userId, starPK.userId) &&
                Objects.equals(targetId, starPK.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, targetId);
    }
}
