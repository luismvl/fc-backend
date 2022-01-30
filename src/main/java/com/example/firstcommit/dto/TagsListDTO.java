package com.example.firstcommit.dto;

import com.example.firstcommit.entities.Tag;

import java.util.List;

public class TagsListDTO {
    List<Tag> tags;

    public TagsListDTO() {
    }

    public TagsListDTO(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
