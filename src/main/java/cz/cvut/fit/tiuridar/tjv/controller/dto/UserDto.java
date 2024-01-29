package cz.cvut.fit.tiuridar.tjv.controller.dto;

import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Date;
@Data
public class UserDto {
    private String username;
    private String realName;
    private Date dateOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
