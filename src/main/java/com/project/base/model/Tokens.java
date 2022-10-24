package com.project.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
@Builder
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", columnDefinition = "text")
    private String token;

    @OneToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Users users;

    @ManyToOne
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RefreshTokens refreshTokens;
}
