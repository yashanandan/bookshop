package com.tw.bootcamp.bookshop.user;

public class CreateUserCommandTestBuilder {
    private CreateUserCommand.CreateUserCommandBuilder commandBuilder;

    public CreateUserCommandTestBuilder() {
        commandBuilder = CreateUserCommand.builder()
                .email("testemail@test.com")
                .password("foobar");
    }

    CreateUserCommand build() {
        return commandBuilder.build();
    }

    public CreateUserCommandTestBuilder withEmptyEmail() {
        commandBuilder.email("");
        return this;
    }

    public CreateUserCommandTestBuilder withEmptyPassword() {
        commandBuilder.password("");
        return this;
    }
}
