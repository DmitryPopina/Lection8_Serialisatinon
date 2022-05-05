import java.io.Serializable;
import java.util.List;

public interface Rabbit extends Serializable {
    @Cache
    int runReturningIntFromMemory(int par) throws InterruptedException;
    @Cache(cacheType = CacheType.FILE)
    int runReturningIntFromFile(int par) throws InterruptedException;
    @Cache(cacheType = CacheType.BOTH)
    int runReturningIntFromMemoryAndFile(int par) throws InterruptedException;
    @Cache(cacheType = CacheType.BOTH, fileNamePrefix = "my", listMaxSize = 100, zip = true)
    List<Integer> runReturningListFromMemoryAndFileLimit100(int size) throws InterruptedException;
}
