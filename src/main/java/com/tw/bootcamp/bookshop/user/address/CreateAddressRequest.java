package com.tw.bootcamp.bookshop.user.address;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateAddressRequest {
    @NotBlank
    private String lineNoOne;
    @NotBlank
    private String lineNoTwo;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String pinCode;
    @NotBlank
    private String country;
}
