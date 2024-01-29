package cz.cvut.fit.tiuridar.tjv.controller.dto;

import lombok.Data;

@Data
public class StreamingServiceDto {
    private Long Id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
