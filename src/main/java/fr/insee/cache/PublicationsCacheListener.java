package fr.insee.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.stereotype.Component;

@Component
public class PublicationsCacheListener implements CacheEventListener<Object, Object> {
	
    @Override
    public void onEvent(CacheEvent<?, ?> event) {
    	switch(event.getType()) {
    		case CREATED:
    			System.out.println(String.format("Ajout dans le cache : %s => %s", event.getKey(), event.getNewValue()));
    		case UPDATED:
    			System.out.println(String.format("Mise à jour du cache : %s : %s => %s", event.getKey(), event.getOldValue(), event.getNewValue()));
    		case REMOVED:
    			System.out.println(String.format("Suppression du cache : %s : %s", event.getKey(), event.getOldValue()));
			default:
				break;
    	}
    }
}
