package org.example.myCacheService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class MyCacheServiceApplication {

	public static void main(String[] args) {
		CacheRepository<String,Bond>  cacheRepo = initializeCacheWithLoadFactor();

		SpringApplication.run(MyCacheServiceApplication.class, args);
	}

	static CacheRepository<String,Bond> initializeCacheWithLoadFactor(){
		try{
			double loadFactor;
			if (System.getenv("LOAD_FACTOR")!=null){
				loadFactor = Double.parseDouble(System.getenv("LOAD_FACTOR"));
				return new CacheRepository<>(loadFactor);

			}
			//use default load factor in case env param wasn't supplied
			return new CacheRepository<>();
		} catch (NumberFormatException e) {
			System.err.println("failed to read env param load factor - using default load factor of 0.75");
			return new CacheRepository<>(CacheRepository.DEFAULT_LOAD_FACTOR);
		}
	}

}
