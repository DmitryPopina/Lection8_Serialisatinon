import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CacheProxy implements InvocationHandler {
    private final Object delegate;
    private final CacheManager cacheManager;

    public CacheProxy(Object delegate) {
        this.cacheManager = new CacheManager();
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!cacheManager.setCache(method, args)) return invoke(method,args);
        Object res = cacheManager.get();
        if (res == null){
            res = invoke(method,args);
            cacheManager.put(res);
        }
        return res;
    }

    private Object invoke(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(delegate,args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible",e);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}