package com.honchi.socket.domain.post;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class PostAttendPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;

    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostAttendPK that = (PostAttendPK) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
