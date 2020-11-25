package com.bridgelabz.bookStore.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassword {
    private String newPassword;
    private String conformPassword;
}
