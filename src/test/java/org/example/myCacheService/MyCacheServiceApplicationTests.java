package org.example.myCacheService;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyCacheServiceApplicationTests {
	CacheRepository<String,Bond>  cacheRepo;
	private static void runLoadTest(int numOfThreads,CacheRepository<String,Bond>  cacheRepo, boolean failedTest, AtomicBoolean hasFailed) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);

		IntStream.range(0,numOfThreads)
				.forEach(id -> executor.execute( ()->new ReadWriteTest(cacheRepo,hasFailed,failedTest).run()));

		executor.shutdown();
		while (!executor.isTerminated()){
			Thread.sleep(200);
		}
	}

	@Test
	void checkCacheWithSingleThreadSuccess() throws InterruptedException {
		cacheRepo = MyCacheServiceApplication.initializeCacheWithLoadFactor();
		AtomicBoolean hasFailed = new AtomicBoolean(false);
		runLoadTest(1,cacheRepo, false,hasFailed);
		assertFalse(hasFailed.get());
	}

	@Test
	void checkCacheWithSingleThreadFailed() throws InterruptedException {
		cacheRepo = MyCacheServiceApplication.initializeCacheWithLoadFactor();
		AtomicBoolean hasFailed = new AtomicBoolean(false);
		runLoadTest(1,cacheRepo, true, hasFailed);
		assertTrue(hasFailed.get());
	}

	@Test
	void checkCacheWithMultiThreadsSuccess1() throws InterruptedException {
		cacheRepo = MyCacheServiceApplication.initializeCacheWithLoadFactor();
		AtomicBoolean hasFailed = new AtomicBoolean(false);
		runLoadTest(5,cacheRepo, false, hasFailed);
		assertFalse(hasFailed.get());
	}

	@Test
	void checkCacheWithMultiThreadsSuccess2() throws InterruptedException {
		cacheRepo = MyCacheServiceApplication.initializeCacheWithLoadFactor();
		AtomicBoolean hasFailed = new AtomicBoolean(false);
		runLoadTest(10,cacheRepo, false, hasFailed);
		assertFalse(hasFailed.get());
	}

}
