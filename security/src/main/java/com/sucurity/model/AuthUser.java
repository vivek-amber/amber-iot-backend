package com.sucurity.model;

import java.util.List;

public record AuthUser (
    String userId,
    String companyId,
    List<String>roles
){
    
}
