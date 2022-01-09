package com.springsecurity.constant;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum RoleName {
    @FieldNameConstants.Include ROLE_USER,
    @FieldNameConstants.Include ROLE_MODERATOR,
    @FieldNameConstants.Include ROLE_ADMIN;
}
