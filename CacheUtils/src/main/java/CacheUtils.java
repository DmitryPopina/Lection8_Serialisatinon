import java.lang.reflect.Proxy;

public class CacheUtils {

    public static Object cache (Object delegate){
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                        delegate.getClass().getInterfaces(),
                                        new CacheProxy(delegate));
    }
}
