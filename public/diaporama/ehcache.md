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


<!-- .slide: data-background-image="images/ehcache-logo.png" data-background-size="600px" class="chapter" -->
## 2
### Installation et configuration


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
### Configuration des caches en XML

ehcache.xml
Exemple minimal :
```xml
<config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.ehcache.org/v3">
	<cache alias="publicationsCache" uses-template="templateCache">
		<heap unit="entries">1000</heap>
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
 - ajout de *listeners* sur les évènements
  - création, suppression, modification, expiration
 - définitions de *serializers*, de *copiers*
  - optimisation fine (les objets du cache sont des copies)
  


===


<!-- .slide: class="slide" -->
### Exemple plus complet

```xml
<!-- Un premier cache typé -->
<cache alias="publicationsCache" uses-template="templateCache" >
	<key-type>java.lang.Long</key-type>
	<value-type>fr.insee.cache.Publication</value-type>
	<resources>
		<heap unit="entries">1000</heap>
		<offheap unit="MB">500</offheap>
	</resources>
</cache>
<!-- Un second cache non typé pour les exceptions -->
<cache alias="exceptionsCache">
	<heap unit="entries">1000</heap>
</cache>
<!-- Un template de cache qui défini des listeners -->
<cache-template name="templateCache"> 
	<listeners>
		<listener>
			<class>fr.insee.cache.PublicationsCacheListener</class>
			<event-firing-mode>ASYNCHRONOUS</event-firing-mode>
			<event-ordering-mode>UNORDERED</event-ordering-mode>
			<events-to-fire-on>CREATED</events-to-fire-on>
			<events-to-fire-on>REMOVED</events-to-fire-on>
			<events-to-fire-on>EXPIRED</events-to-fire-on>
			<events-to-fire-on>UPDATED</events-to-fire-on>
		</listener>
	</listeners>
</cache-template>
```


===


<!-- .slide: class="slide" -->
### TODO Remarques

jCache vs Ehcache vs Spring cache

Configuration Java / XML


===


<!-- .slide: data-background-image="images/ehcache-logo.png" data-background-size="600px" class="chapter" -->
## 3
### Clés de cache


===


<!-- .slide: class="slide" -->
### Clés de cache (1)
A. La fonction ne prends qu’un seul paramètre
 - la clé est ce paramètre
 - attention aux méthode *hashCode* et *equals*

```java
@CacheResult(cacheName = "publicationsCache")
public Publication genererPublication(Long id) {...}

@CacheResult(cacheName = "publicationsCache")
public Publication publicationAssociee(Panorama panorama) {...}
```
 
B. La fonction ne prend aucun paramètre
 - utiliser un cache spécifique

```java
@CacheResult(cacheName = "toutesPublicationsCache")
public List<Publication> toutesPublications() {...}
```
  
```xml
<cache alias="toutesPublicationsCache">
	<heap unit="entries">1</heap>
</cache>
```


===


<!-- .slide: class="slide" -->
### Clés de cache (2)

C. La fonction prend plusieurs paramètres

 - utiliser l’annotation `@CacheKey` sur un seul paramètre &rarr; cas A.
 
```java
@CacheResult(cacheName = "publicationsCache")
public String genererPublication(@CacheKey Publication publication, String contextPath) {...}
```

 - La clé est composite : `SimpleKey`

```java
@CacheResult(cacheName = "publicationsCache")
public String genererPublicationAvecSommaire(Publication publication, Sommaire sommaire) {...}
```

D. On peut utiliser un `cacheKeyGenerator`
 - pour tous les cas plus compliqués
 - ou pour le cas des fonctions sans paramètres

```java
@CacheResult(cacheName = "publicationsCache", cacheKeyGenerator = NoParamKeyGenerator.class)
public List<Publication> findLatest() {...}
```

===


<!-- .slide: class="slide" -->
### Exemple de générateur de clé

```java
public class NoParamKeyGenerator implements CacheKeyGenerator {

	@Override
	public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> context) {
		String key = context.getTarget().getClass().getName() + "." + context.getMethod().getName();
		return new NoParamKey(key);
	}

	static class NoParamKey implements GeneratedCacheKey {
		
		private String key;
		
		public NoParamKey(String key) { this.key = key; }

		@Override
		public int hashCode() { return key.hashCode(); }
		
		@Override
		public boolean equals(Object object) {
			if(object instanceof NoParamKey) {
				NoParamKey other = (NoParamKey) object;
				return this.key.equals(other.key);
			}
			return false;
		}
	}
}
```
