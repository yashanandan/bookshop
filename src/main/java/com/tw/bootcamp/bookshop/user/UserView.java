package com.tw.bootcamp.bookshop.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserView {
    private final String id;
    private final String email;

    public UserView(User user) {
        this.id = user.getId().toString();
        this.email = user.getEmail();
    }
}
