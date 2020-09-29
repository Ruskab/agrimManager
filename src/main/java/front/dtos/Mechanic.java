package front.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mechanic implements Serializable {

    private int id;

    private String name;

    private String password;

    private List<Integer> interventionIds = new ArrayList<>();

    private Set<String> roles = new HashSet<>();

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public void addRole(String role) {
        if (!hasRole(role)) {
            roles.add(role);
        }
    }

    public boolean anyRoleGranted(String... roles) {
        for (String role : roles) {
            if (this.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}
