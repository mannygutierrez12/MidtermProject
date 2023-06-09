package com.skilldistillery.jpaeventlight.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "venue_comment")
public class VenueComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String comment;
	
	@Column(name="comment_date")
	private LocalDateTime commentDate;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name="venue_id")
	private Venue venue;
	
	@ManyToOne
	@JoinColumn(name="reply_to_id")
	private VenueComment parentComment;
	
	@OneToMany(mappedBy="parentComment")
	private List<VenueComment> replies;
	
	
	
	public VenueComment() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public VenueComment getParentComment() {
		return parentComment;
	}

	public void setParentComment(VenueComment parentComment) {
		this.parentComment = parentComment;
	}

	public List<VenueComment> getReplies() {
		return replies;
	}

	public void setReplies(List<VenueComment> replies) {
		this.replies = replies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VenueComment other = (VenueComment) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "VenueComment [id=" + id + "]";
	}
	
	
	
	

}
