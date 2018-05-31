package fr.insee.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publications")
public class PublicationsController {

	@Autowired
	private PublicationsService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Publication>> publications() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Publication>> latestPublications() {
		return ResponseEntity.ok(service.findLatest());
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Publication> publication(@PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok(service.findOne(id));
		}
		catch (NoResultFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<Void> updatePublication(@PathVariable Long id, @RequestBody Publication publication) {
		try {
			service.update(id, publication);
			return ResponseEntity.ok().build();
		}
		catch (NoResultFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Publication> deletePublication(@PathVariable Long id) {
		try {
			service.delete(id);
			return ResponseEntity.ok().build();
		}
		catch (NoResultFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
