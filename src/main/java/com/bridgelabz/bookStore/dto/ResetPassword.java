package com.bridgelabz.bookStore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassword {
    private String newPassword;
    private String conformPassword;
}
