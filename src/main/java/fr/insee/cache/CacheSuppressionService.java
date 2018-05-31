package fr.insee.cache;

import javax.cache.annotation.CacheRemoveAll;

import org.springframework.stereotype.Service;

@Service
public class CacheSuppressionService {
	
	@CacheRemoveAll(cacheName = "listePublicationsCache")
    public void viderCacheListePublications() {
    	System.out.println("viderCacheListePublications");
    }
}
