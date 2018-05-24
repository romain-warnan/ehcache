<!-- .slide: data-background-image="images/ehcache-logo.png" data-background-size="600px" class="chapter" -->
## 1
### Concepts de base


===


<!-- .slide: class="slide" -->
### Qu’est ce qu’un cache applicatif ?

Conserver le résultat de certaines fonctions
 - utilisation de mémoire &#8599;
 - temps de calcul &#8600;

Le résultat est associé aux paramètres d’appel
 - clé de cache


===


<!-- .slide: class="slide" -->
### Un cache géré « à la main »
Sans cache 
```java
public Publication genererPublication(Long id) {
	Publication publication = … // Calculs coûteux
	return publication;
}
```

Avec cache
```java
private static final Map<Object, Object> publicationsCache = new HashMap<>();

public Publication genererPublication(Long id) {
	if(publicationsCache.containsKey(id)) { // Le cache contient-il le résultat ?
		return (Publication) publicationsCache.get(id); // Oui : il est simplement retourné
	}
	Publication publication = … // Non : calculs coûteux
	publicationsCache.put(id, publication); // Ajouter le résultat dans le cache
	return publication;
}
```


===


<!-- .slide: class="slide" -->
### Cache géré par Spring
Sans cache 
```java
public Publication genererPublication(Long id) {
	Publication publication = … // Calculs coûteux
	return publication;
}
```

Avec cache
```java
@CacheResult(cacheName = "publicationsCache") // Une simple déclaration suffit
public Publication genererPublication(Long id) {
	Publication publication = … // Calculs coûteux
	return publication;
}
```


===


<!-- .slide: class="slide" -->
### Mise en place du cache avec Spring

pom.xml
```xml
<dependency> <!-- Spécification : Cache, Cache.Entry, CacheManager et CacheProvider -->
	<groupId>javax.cache</groupId>
	<artifactId>cache-api</artifactId> 
</dependency>
<dependency> <!-- Implémentation -->
	<groupId>org.ehcache</groupId>
	<artifactId>ehcache</artifactId> 
</dependency>
```

CacheConfig.java
```java
@Configuration
@EnableCaching // Active le cache
public class CacheConfig {
	// Déclaration de beans
}
```

 application.properties
```
spring.cache.jcache.config=classpath:ehcache.xml
```


===


<!-- .slide: class="slide" -->
Configuration des caches en XML

ehcache.xml
```xml
<config xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.ehcache.org/v3'>
	<cache alias="publicationsCache">
		<resources>
			<heap unit="entries">1000</heap>
			<offheap unit="MB">100</offheap>
		</resources>
	</cache>
</config>
```

Les possibilités :
 - durée de vie des éléments du cache
  - éternel, une minute…
 - typage du cache
  - clés et valeurs
 - stockage et taille du cache
  - *heap*, *swap*, 100 MB, 1000 entrées…
 - stockage persistent après redémarrage
 - factorisation possible grâce à la notion de *template* de cache
 - définitions de *serializers*, de *copiers*
  - optimisation fine (les objets du cache sont des copies)