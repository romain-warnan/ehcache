package fr.insee.cache;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.springframework.stereotype.Service;

@Service
public class PublicationsService {

	private static final List<Publication> repository = new ArrayList<>();
	
	public PublicationsService() {
		repository.add(Publication.of(1L, "Principaux indices et séries chronologiques", LocalDate.of(2018, 1, 12)));
		repository.add(Publication.of(2L, "La région dépasse les huit millions d’habitants au 1er janvier 2018", LocalDate.of(2018,  5, 23)));
		repository.add(Publication.of(3L, "Enquête Flux Touristiques", LocalDate.of(2018,  5, 17)));
		repository.add(Publication.of(4L, "Dans les communes les moins dotées en services, artisans du bâtiment et restaurants sont les plus présents", LocalDate.of(2018,  4, 3)));
		repository.add(Publication.of(5L, "Emploi, activité, sous-emploi par secteur d’activité (sens BIT)", LocalDate.of(2017, 12, 21)));
		repository.add(Publication.of(6L, "Description des emplois privés et publics et des salaires en 2015", LocalDate.of(2017,  11, 4)));
		repository.add(Publication.of(7L, "Indices des prix à la consommation", LocalDate.of(2017,  6, 1)));
		repository.add(Publication.of(8L, "Avril 2018 : Hausse des prix de 0,3 % en deux mois", LocalDate.of(2018,  3, 2)));
		repository.add(Publication.of(9L, "Tableau de bord de la conjoncture", LocalDate.of(2018,  2, 12)));
		repository.add(Publication.of(10L, "Taux de mortalité par cause de décès selon le sexe dans l'Union européenne en 2015", LocalDate.of(2018,  5, 24)));
		System.out.println(repository.hashCode());
	}
	
	
	
	@CacheResult(cacheName = "listePublicationsCache", cacheKeyGenerator = NoParamKeyGenerator.class)
	public List<Publication> findAll() {
		this.search(3);
		return repository;
	}
	
	@CacheResult(cacheName = "listePublicationsCache", cacheKeyGenerator = NoParamKeyGenerator.class)
	public List<Publication> findLatest() {
		this.search(3);
		return repository.stream()
			.sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
			.limit(3)
			.collect(Collectors.toList());
	}
	
	@CacheResult(cacheName = "publicationsCache")
	public Publication findOne(Long id) throws NoResultFoundException {
		this.search(3);
		return repository.stream()
			.filter(p -> p.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new NoResultFoundException());
	}
	
	@CachePut(cacheName = "publicationsCache")
	public void update(Long id, @CacheValue Publication publication) throws NoResultFoundException {
		Publication oldPublication = this.findOne(id);
		oldPublication.setTitle(publication.getTitle());
		oldPublication.setDate(publication.getDate());
	}
	
	@CacheRemove(cacheName = "publicationsCache")
	public void delete(Long id) throws NoResultFoundException {
		repository.remove(this.findOne(id));
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
