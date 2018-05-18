package fr.insee.cache;

public class Publication {

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
}
