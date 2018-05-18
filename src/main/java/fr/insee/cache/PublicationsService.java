package fr.insee.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class PublicationsService {

	private static final List<Publication> repository = new ArrayList<>();
	
	public PublicationsService() {
		repository.add(Publication.of(1L, "Principaux indices et séries chronologiques"));
		repository.add(Publication.of(2L, "Estimations d'emploi salarié par secteur d'activité"));
		repository.add(Publication.of(3L, "Enquête Flux Touristiques"));
	}
	
	public List<Publication> findAll() {
		this.search(5);
		return repository;
	}
	
	public Optional<Publication> findOne(Long id) {
		this.search(3);
		return repository.stream()
			.filter(p -> p.getId().equals(id))
			.findFirst();
	}
	
	private void search(long time) {
		try {
			TimeUnit.SECONDS.sleep(time);
		}
		catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
