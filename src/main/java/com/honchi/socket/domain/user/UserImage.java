package com.honchi.socket.domain.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserImage {

    @Id
    private Integer userId;

    private String imageName;
}
