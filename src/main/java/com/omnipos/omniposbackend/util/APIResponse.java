package com.omnipos.omniposbackend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIResponse<T>{
    private int statusCode;
    private String message;
    private T data;
}
