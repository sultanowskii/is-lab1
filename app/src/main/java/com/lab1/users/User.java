package com.lab1.users;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private int id;

    @NotBlank
    @Size(min = 3, max = 32)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type;

    @JsonIgnore
    public boolean isAdmin() {
        return this.type.equals(UserType.ADMIN);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
		return true;
	}

    @Override
    @JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

    @Override
    @JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

    
    @Override
    @JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}
