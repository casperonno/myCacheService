package org.example.myCacheService;
import java.util.concurrent.atomic.AtomicBoolean;
import static  org.junit.jupiter.api.Assertions.assertEquals;



public class ReadWriteTest implements Runnable{

    private final boolean failedTest;
    CacheRepository<String,Bond>  cacheRepo;
    AtomicBoolean hasFailed;
    Bond[] bondsData = new Bond[MAX_OPERATIONS_PER_THREAD];
    static final Integer MAX_OPERATIONS_PER_THREAD = 10;


    public ReadWriteTest(CacheRepository<String,Bond> cacheRepo,AtomicBoolean hasFailed, boolean failedTest){
        this.cacheRepo = cacheRepo;
        this.hasFailed = hasFailed;
        this.failedTest = failedTest;
    }

    @Override
    public void run() {
        String prefix = Thread.currentThread().getName() + "_";
        for (int i = 0; i< MAX_OPERATIONS_PER_THREAD; i++){
            String currPrefix = i+prefix;
            Bond bond= new Bond(currPrefix+"bondId",currPrefix+"exchange",currPrefix+"name",currPrefix+"secType",currPrefix+"description",currPrefix+"currency",currPrefix+"country");
            bondsData[i] = bond;
            cacheRepo.put(bond.getBondId(),bond);
        }

        for (int i = 0; i< MAX_OPERATIONS_PER_THREAD; i++){
            String currPrefix = i+prefix+"bondId";
            if (failedTest){
                currPrefix+="failed";
            }
            Bond bondRes = cacheRepo.get(currPrefix);
            try {
                assertEquals(bondRes,bondsData[i]);
            } catch (AssertionError e){
                this.hasFailed.set(true);
                break;
            }
        }
    }
}
