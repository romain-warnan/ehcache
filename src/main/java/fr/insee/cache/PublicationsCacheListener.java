package fr.insee.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.springframework.stereotype.Component;

@Component
public class PublicationsCacheListener implements CacheEventListener<Object, Object> {
	
    @Override
    public void onEvent(CacheEvent<?, ?> event) {
    	if(event.getType() == EventType.CREATED) {
    		System.out.println(String.format("Ajout dans le cache : %s => %s", event.getKey(), event.getNewValue()));
    	}
    }
}
