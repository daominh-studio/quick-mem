package com.daominh.quickmem.data.model;

import java.util.UUID;

public class Card {
    String id;
    String front;
    String back;
    int status;
    int isLearned;
    String flashcard_id;
    String created_at;
    String updated_at;

    public Card() {
    }

    public Card(String front, String back, int status, int isLearned, String flashcard_id, String created_at, String updated_at) {
        this.id = UUID.randomUUID().toString();
        this.front = front;
        this.back = back;
        this.status = status;
        this.isLearned = isLearned;
        this.flashcard_id = flashcard_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getFlashcard_id() {
        return flashcard_id;
    }

    public void setFlashcard_id(String flashcard_id) {
        this.flashcard_id = flashcard_id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsLearned() {
        return isLearned;
    }

    public void setIsLearned(int isLearned) {
        this.isLearned = isLearned;
    }
}
