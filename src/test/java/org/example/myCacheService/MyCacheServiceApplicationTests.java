package org.example.myCacheService;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyCacheServiceApplicationTests {
	CacheRepository<String,Bond>  cacheRepo;
	private static void runLoadTest(int numOfThreads,CacheRepository<String,Bond>  cacheRepo, boolean failedTest, AtomicBoolean hasFailed) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
		List<ReadWriteTest> readWriteTestThreads = new ArrayList<>();


		for (int i=0; i<numOfThreads; i++){
			ReadWriteTest readWriteTestThread = new ReadWriteTest(cacheRepo,hasFailed,failedTest);
			readWriteTestThreads.add(readWriteTestThread);
			executor.submit(readWriteTestThread);
		}
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
		runLoadTest(2,cacheRepo, false, hasFailed);
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
