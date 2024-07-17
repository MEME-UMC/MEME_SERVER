package org.meme.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Artist extends User {

    @Column(nullable = true, length = 500)
    private String introduction;
}