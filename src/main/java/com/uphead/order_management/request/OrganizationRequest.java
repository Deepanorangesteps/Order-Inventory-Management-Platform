package com.uphead.order_management.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrganizationRequest { private String code;
private String name;

public OrganizationRequest() {
}

public String getCode() {
    return code;
}

public void setCode(String code) {
    this.code = code;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

@Override
public String toString() {
    return "OrganizationRequest{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            '}';
}}
