package com.skilldistillery.jpaeventlight.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Venue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name="phone_number")
	private String phoneNumber;
	
	private String description;
	
	private String name;
	
	private String picture;
	
	@JoinColumn(name="picture_logo")
	private String pictureLogo;
	
	@JoinColumn(name="created_at")
	private LocalDateTime createdAt;
	
	@JoinColumn(name="last_update")
	private LocalDateTime lastUpdate;

	public Venue() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPictureLogo() {
		return pictureLogo;
	}

	public void setPictureLogo(String pictureLogo) {
		this.pictureLogo = pictureLogo;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
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
		Venue other = (Venue) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Venue [id=" + id + ", phoneNumber=" + phoneNumber + ", description=" + description + ", name=" + name
				+ ", picture=" + picture + ", pictureLogo=" + pictureLogo + ", createdAt=" + createdAt + ", lastUpdate="
				+ lastUpdate + "]";
	}

	
}
