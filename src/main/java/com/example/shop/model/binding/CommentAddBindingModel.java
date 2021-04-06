package com.example.shop.model.binding;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CommentAddBindingModel {

    private String message;
    private Integer rating;

    public CommentAddBindingModel() {
    }

    @Size(min = 5, max = 100)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Min(1)
    @Max(5)
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
