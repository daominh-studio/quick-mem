package com.daominh.quickmem.data.model;


import java.util.Objects;

public class Card {
    private String id;
    private String front;
    private String back;
    private int status;
    private int isLearned;
    private String flashcard_id;
    private String created_at;
    private String updated_at;

    public Card() {
    }

    public Card(String front, String back, int status, int isLearned, String flashcard_id, String created_at, String updated_at) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return (id == null ? card.id == null : id.equals(card.id)) &&
                (front == null ? card.front == null : front.equals(card.front)) &&
                (back == null ? card.back == null : back.equals(card.back));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, front, back);
    }
}
