package fr.insee.cache;

import java.lang.annotation.Annotation;

import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

public class NoParamKeyGenerator implements CacheKeyGenerator {

	@Override
	public GeneratedCacheKey generateCacheKey(CacheKeyInvocationContext<? extends Annotation> context) {
		String key = context.getTarget().getClass().getName() + "." + context.getMethod().getName();
		return new NoParamKey(key);
	}

	@SuppressWarnings("serial")
	static class NoParamKey implements GeneratedCacheKey {
		
		private String key;
		
		public NoParamKey(String key) {
			super();
			this.key = key;
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}
		
		@Override
		public boolean equals(Object object) {
			if(object instanceof NoParamKey) {
				NoParamKey other = (NoParamKey) object;
				return this.key.equals(other.key);
			}
			return false;
		}

		@Override
		public String toString() {
			return key;
		}
	}
}
