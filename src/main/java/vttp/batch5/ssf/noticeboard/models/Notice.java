package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {

    @NotEmpty(message = "Notice title cannot be empty")
    @NotNull(message = "Notice title cannot be null")
    @Size(min = 3, max = 128, message = "Notice title's length must be between 3 and 128 characters")
    private String title;

    @NotEmpty(message = "Notice poster's email cannot be empty")
    @NotNull(message = "Notice poster's email cannot be null")
    @Email(message = "Must be a well-formed email address")
    private String poster;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Post date must be a date in the future")
    private Date postDate;

    private List<String> categories;

    @NotEmpty(message = "Contents of the notice cannot be empty")
    @NotNull(message = "Contents of the notice cannot be null")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Notice [title=" + title + ", poster=" + poster + ", postDate=" + postDate + ", categories=" + categories
                + ", text=" + text + "]";
    }
}
