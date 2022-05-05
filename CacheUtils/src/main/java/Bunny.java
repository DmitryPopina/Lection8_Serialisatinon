import java.util.ArrayList;
import java.util.List;

public class Bunny implements Rabbit {

    @Override
    public int runReturningIntFromMemory(int par) throws InterruptedException {
        Thread.sleep(3000);
        return par;
    }
    @Override
    public int runReturningIntFromFile(int par) throws InterruptedException {
        Thread.sleep(3000);
        return par;
    }
    @Override
    public int runReturningIntFromMemoryAndFile(int par) throws InterruptedException {
        Thread.sleep(3000);
        return par;
    }
    @Override
    public List<Integer> runReturningListFromMemoryAndFileLimit100(int size) throws InterruptedException {
        var res = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
        {
            res.add(i);
        }
        Thread.sleep(3000);
        return res;
    }
}
