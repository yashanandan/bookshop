package com.tw.bootcamp.bookshop.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;

    public UserView toView() {
        return UserView.builder()
                .id(id.toString())
                .email(email)
                .build();
    }

    public Long getId() {
        return id;
    }
}
