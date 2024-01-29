package cz.cvut.fit.tiuridar.tjv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user_account")
public class User implements EntityWithId<String> {
    @Id
    private String username;
    private String realName;
    private Date dateOfBirth;
    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Collection<Review> writtenByUser = new ArrayList<>();

    private String password;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof User u)
            return username == null ? username == u.username : username.equals(u.username);
        return false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Review> getWrittenByUser() {
        return writtenByUser;
    }
    public void setWrittenByUser(Collection<Review> writtenByUser) {
        this.writtenByUser = writtenByUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() { return realName; }

    public void setRealName(String realName) { this.realName = realName; }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getId() { return username; }
}
