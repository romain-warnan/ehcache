package fr.insee.cache;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheConfig {

	@Autowired
	private CacheManager cacheManager;

	@PostConstruct
	public void postConstruct() {
		cacheManager.getCacheNames().forEach(System.out::println);
	}
}
