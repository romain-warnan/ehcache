package fr.insee.cache;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class Publication implements Serializable {

	private Long id;
	private String title;
	private LocalDate date;
	
	private Publication(Long id, String title, LocalDate date) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
	}

	public static Publication of(Long id) {
		return new Publication(id, null, null);
	}
	
	public static Publication of(Long id, String title, LocalDate date) {
		return new Publication(id, title, date);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return String.format("Publication {id: %d, title: %s, date: %s}", id, title, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}
	
	
}
