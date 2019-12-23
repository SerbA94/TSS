package ua.tss.model.enums;



import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER,
    ADMIN,
    SUPERVISOR;

    @Override
    public String getAuthority() {
        return name();
    }
}