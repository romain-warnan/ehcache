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
@Cacheable("publicationsCache") // Une simple déclaration suffit
public Publication genererPublication(Long id) {
	Publication publication = … // Calculs coûteux
	return publication;
}
```


===


<!-- .slide: class="slide" -->
### Mise en place du cache avec Spring
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

```java
@Configuration
@EnableCaching
public class CacheConfig {

	@Autowired
	private CacheManager cacheManager;

	@PostConstruct
	public void postConstruct() {
		cacheManager.getCacheNames().forEach(System.out::println);
	}
}
```
