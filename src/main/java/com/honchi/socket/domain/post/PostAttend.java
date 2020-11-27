package com.honchi.socket.domain.post;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(PostAttendPK.class)
public class PostAttend implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer postId;

    @Id
    private Integer userId;
}
