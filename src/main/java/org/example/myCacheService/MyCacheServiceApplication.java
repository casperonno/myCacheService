package org.example.myCacheService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyCacheServiceApplication {

	public static void main(String[] args) {
		CacheRepository<String,Bond>  cacheRepo = initializeCacheWithLoadFactor();
//		Bond b1 = new Bond("01A56DE6-7DB5-4179-B988-B57FE1889488","CBT", "US 5YR NOTE (CBT) Sep1", "FUTURE","FVU14 COMB","USD","US");
//		Bond b2 = new Bond("01A56DE6-7DB5-4179-B988-B57FE1889499","2", "2", "2","2","2","2");
//		cacheRepo.put(b1.getKey(),b1.getData());
//		cacheRepo.put(b2.getKey(),b2.getData());
//		Bond bond = cacheRepo.get(b2.getKey());

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
