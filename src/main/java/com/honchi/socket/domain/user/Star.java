package com.honchi.socket.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StarPK.class)
public class Star implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer userId;

    @Id
    private Integer targetId;

    private Double star;
}
