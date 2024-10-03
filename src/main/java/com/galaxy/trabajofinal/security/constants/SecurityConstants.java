package com.galaxy.trabajofinal.security.constants;

public interface SecurityConstants {
    String SUPER_SECRET_KEY = "713F4428472B4B6250655368566D5970337336763979244226452948404A622";
    long TOKEN_EXPIRATION_TIME = 86_400_000; // 1 day 86_400_000 // Milisegundos - 3 Minutos 1_000*60*60 => 3_600_000
}
