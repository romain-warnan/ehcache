package fr.insee.cache;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Publication implements Serializable {

	private Long id;
	private String title;
	
	private Publication(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public static Publication of(Long id, String title) {
		return new Publication(id, title);
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

	@Override
	public String toString() {
		return String.format("Publication {id: %d, title: %s}", id, title);
	}
	
	
}
