package com.atoz_develop.restapipractice.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    // 여러개의 enum을 가질 수 있음
    // Set, List의 기본 fetch 타입이 LAZY인데 가져올 role이 적고 자주 사용되므로 EAGER로 설정
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
