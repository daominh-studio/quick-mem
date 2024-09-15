package com.daominh.quickmem.data.model;

import java.util.UUID;

public class FlashCard {
    private String id;
    private String name;
    private String description;
    private int is_public;
    private String created_at;
    private String updated_at;

    public FlashCard() {
    }

    public FlashCard(String name, String description, int is_public, String created_at, String updated_at) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.is_public = is_public;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }
}
