package fr.insee.cache;

import javax.cache.CacheManager;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublicationsCacheListener implements CacheEventListener<Object, Object> {
	
	@Autowired
	private CacheManager cacheManager;
	
    @Override
    public void onEvent(CacheEvent<?, ?> event) {
    	System.out.println(event.getSource().toString() + " : " + event.getType());
    	event.getSource().forEach(e -> System.out.println(e.getKey() + " => " + e.getValue().hashCode()));
    	
    	switch(event.getType()) {
    		case UPDATED:
    		case REMOVED:
    			cacheManager.getCache("listePublicationsCache").removeAll();
    			break;
			default:
				break;
    	}
    }
}
