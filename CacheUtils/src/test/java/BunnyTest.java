import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class BunnyTest {
    Rabbit bunny = (Rabbit) CacheUtils.cache(new Bunny());

    @Test
    void runReturningIntFromMemory() throws InterruptedException {
        System.out.println("First run " + LocalDateTime.now());
        bunny.runReturningIntFromMemory(5);
        System.out.println("After first run " + LocalDateTime.now());
        System.out.println("Second run " + LocalDateTime.now());
        bunny.runReturningIntFromMemory(5);
        System.out.println("After second run " + LocalDateTime.now());
    }

    @Test
    void runReturningIntFromFile() throws InterruptedException {
        System.out.println("First run " + LocalDateTime.now());
        bunny.runReturningIntFromFile(5);
        System.out.println("After first run " + LocalDateTime.now());
        System.out.println("Second run " + LocalDateTime.now());
        bunny.runReturningIntFromFile(5);
        System.out.println("After second run " + LocalDateTime.now());
    }

    @Test
    void runReturningIntFromMemoryAndFile() throws InterruptedException {
        System.out.println("First run " + LocalDateTime.now());
        bunny.runReturningIntFromMemoryAndFile(5);
        System.out.println("After first run " + LocalDateTime.now());
        System.out.println("Second run " + LocalDateTime.now());
        bunny.runReturningIntFromMemoryAndFile(5);
        System.out.println("After second run " + LocalDateTime.now());
    }

    @Test
    void runReturningListFromMemoryAndFileLimit100() throws InterruptedException {
        System.out.println("First run " + LocalDateTime.now());
        System.out.println(bunny.runReturningListFromMemoryAndFileLimit100(200));
        System.out.println("After first run " + LocalDateTime.now());
        System.out.println("Second run " + LocalDateTime.now());
        System.out.println(bunny.runReturningListFromMemoryAndFileLimit100(200));
        System.out.println("After second run " + LocalDateTime.now());
    }
}
