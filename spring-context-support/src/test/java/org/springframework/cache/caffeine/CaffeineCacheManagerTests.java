/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cache.caffeine;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.junit.jupiter.api.Test;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

/**
 * @author Ben Manes
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 */
public class CaffeineCacheManagerTests {

	@Test
	@SuppressWarnings("cast")
	public void testDynamicMode() {
		CacheManager cm = new CaffeineCacheManager();

		Cache cache1 = cm.getCache("c1");
		assertThat(cache1).isInstanceOf(CaffeineCache.class);
		Cache cache1again = cm.getCache("c1");
		assertThat(cache1).isSameAs(cache1again);
		Cache cache2 = cm.getCache("c2");
		assertThat(cache2).isInstanceOf(CaffeineCache.class);
		Cache cache2again = cm.getCache("c2");
		assertThat(cache2).isSameAs(cache2again);
		Cache cache3 = cm.getCache("c3");
		assertThat(cache3).isInstanceOf(CaffeineCache.class);
		Cache cache3again = cm.getCache("c3");
		assertThat(cache3).isSameAs(cache3again);

		cache1.put("key1", "value1");
		assertThat(cache1.get("key1").get()).isEqualTo("value1");
		cache1.put("key2", 2);
		assertThat(cache1.get("key2").get()).isEqualTo(2);
		cache1.put("key3", null);
		assertThat(cache1.get("key3").get()).isNull();
		cache1.evict("key3");
		assertThat(cache1.get("key3")).isNull();
		assertThat(cache1.get("key3", () -> "value3")).isEqualTo("value3");
		assertThat(cache1.get("key3", () -> "value3")).isEqualTo("value3");
		cache1.evict("key3");
		assertThat(cache1.get("key3", () -> (String) null)).isNull();
		assertThat(cache1.get("key3", () -> (String) null)).isNull();
	}

	@Test
	public void testStaticMode() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1", "c2");

		Cache cache1 = cm.getCache("c1");
		assertThat(cache1).isInstanceOf(CaffeineCache.class);
		Cache cache1again = cm.getCache("c1");
		assertThat(cache1).isSameAs(cache1again);
		Cache cache2 = cm.getCache("c2");
		assertThat(cache2).isInstanceOf(CaffeineCache.class);
		Cache cache2again = cm.getCache("c2");
		assertThat(cache2).isSameAs(cache2again);
		Cache cache3 = cm.getCache("c3");
		assertThat(cache3).isNull();

		cache1.put("key1", "value1");
		assertThat(cache1.get("key1").get()).isEqualTo("value1");
		cache1.put("key2", 2);
		assertThat(cache1.get("key2").get()).isEqualTo(2);
		cache1.put("key3", null);
		assertThat(cache1.get("key3").get()).isNull();
		cache1.evict("key3");
		assertThat(cache1.get("key3")).isNull();

		cm.setAllowNullValues(false);
		Cache cache1x = cm.getCache("c1");
		assertThat(cache1x).isInstanceOf(CaffeineCache.class);
		assertThat(cache1x).isNotSameAs(cache1);
		Cache cache2x = cm.getCache("c2");
		assertThat(cache2x).isInstanceOf(CaffeineCache.class);
		assertThat(cache2x).isNotSameAs(cache2);
		Cache cache3x = cm.getCache("c3");
		assertThat(cache3x).isNull();

		cache1x.put("key1", "value1");
		assertThat(cache1x.get("key1").get()).isEqualTo("value1");
		cache1x.put("key2", 2);
		assertThat(cache1x.get("key2").get()).isEqualTo(2);

		cm.setAllowNullValues(true);
		Cache cache1y = cm.getCache("c1");

		cache1y.put("key3", null);
		assertThat(cache1y.get("key3").get()).isNull();
		cache1y.evict("key3");
		assertThat(cache1y.get("key3")).isNull();
	}

	@Test
	public void changeCaffeineRecreateCache() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		Cache cache1 = cm.getCache("c1");

		Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(10);
		cm.setCaffeine(caffeine);
		Cache cache1x = cm.getCache("c1");
		assertThat(cache1x).isNotSameAs(cache1);

		cm.setCaffeine(caffeine);  // Set same instance
		Cache cache1xx = cm.getCache("c1");
		assertThat(cache1xx).isSameAs(cache1x);
	}

	@Test
	public void changeCaffeineSpecRecreateCache() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		Cache cache1 = cm.getCache("c1");

		cm.setCaffeineSpec(CaffeineSpec.parse("maximumSize=10"));
		Cache cache1x = cm.getCache("c1");
		assertThat(cache1x).isNotSameAs(cache1);
	}

	@Test
	public void changeCacheSpecificationRecreateCache() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		Cache cache1 = cm.getCache("c1");

		cm.setCacheSpecification("maximumSize=10");
		Cache cache1x = cm.getCache("c1");
		assertThat(cache1x).isNotSameAs(cache1);
	}

	@Test
	public void changeCacheLoaderRecreateCache() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		Cache cache1 = cm.getCache("c1");

		@SuppressWarnings("unchecked")
		CacheLoader<Object, Object> loader = mock();

		cm.setCacheLoader(loader);
		Cache cache1x = cm.getCache("c1");
		assertThat(cache1x).isNotSameAs(cache1);

		cm.setCacheLoader(loader);  // Set same instance
		Cache cache1xx = cm.getCache("c1");
		assertThat(cache1xx).isSameAs(cache1x);
	}

	@Test
	public void setCacheNameNullRestoreDynamicMode() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		assertThat(cm.getCache("someCache")).isNull();
		cm.setCacheNames(null);
		assertThat(cm.getCache("someCache")).isNotNull();
	}

	@Test
	public void cacheLoaderUseLoadingCache() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		cm.setCacheLoader(key -> {
			if ("ping".equals(key)) {
				return "pong";
			}
			throw new IllegalArgumentException("I only know ping");
		});
		Cache cache1 = cm.getCache("c1");
		Cache.ValueWrapper value = cache1.get("ping");
		assertThat(value).isNotNull();
		assertThat(value.get()).isEqualTo("pong");

		assertThatIllegalArgumentException().isThrownBy(() -> assertThat(cache1.get("foo")).isNull())
				.withMessageContaining("I only know ping");
	}

	@Test
	public void customCacheRegistration() {
		CaffeineCacheManager cm = new CaffeineCacheManager("c1");
		com.github.benmanes.caffeine.cache.Cache<Object, Object> nc = Caffeine.newBuilder().build();
		cm.registerCustomCache("c2", nc);

		Cache cache1 = cm.getCache("c1");
		Cache cache2 = cm.getCache("c2");
		assertThat(nc).isSameAs(cache2.getNativeCache());

		cm.setCaffeine(Caffeine.newBuilder().maximumSize(10));
		assertThat(cm.getCache("c1")).isNotSameAs(cache1);
		assertThat(cm.getCache("c2")).isSameAs(cache2);
	}

}
