import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CacheManager {

    private CacheType cacheType;
    private String fileName;
    private boolean zip;
    private int[] identityBy;
    private int listMaxSize;
    private final HashMap<Object, Object> memoryCache;
    private List<Object> key;

    public CacheManager(){
        memoryCache = new HashMap<>();
    }

    public boolean setCache(Method method, Object[] args){
        if (!method.isAnnotationPresent(Cache.class)) {
            return false;
        }
        if (void.class.equals(method.getReturnType())){
            throw new RuntimeException("Ошибочная установка признака кэширования для метода без результата. Добавьте возвращаемый результат или уберите признак.");
        }
        Cache cache = method.getAnnotation(Cache.class);
        this.cacheType = cache.cacheType();
        this.zip = cache.zip();
        this.identityBy = cache.identityBy();
        this.listMaxSize = cache.listMaxSize();
        setKey(method, args);
        setFileName(cache.fileNamePrefix(), method.getName());
        return true;
    }

    private void setFileName(String prefix, String methodName){
        this.fileName = (prefix.equals("")  ? "" : prefix + "_") + methodName + "_" + key.hashCode() + (this.zip ? ".zip" : ".dat");
    }

    private void toFile(Object result){
        if (this.zip)
        {
            try(var zipOut = new ZipOutputStream(new FileOutputStream(this.fileName));
                var bos = new ByteArrayOutputStream();
                var out = new ObjectOutputStream(bos))
            {
                zipOut.putNextEntry(new ZipEntry(fileName.replaceAll("zip$", "dat")));
                out.writeObject(result);
                out.flush();
                zipOut.write(bos.toByteArray());
                zipOut.closeEntry();
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try (var fos = new FileOutputStream(fileName);
                 var out = new ObjectOutputStream(fos)) {
                out.writeObject(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object fromFile(){
        if (this.zip) {
            try (var zipFile = new ZipFile(this.fileName);
                 var inputStream = zipFile.getInputStream(zipFile.entries().nextElement());
                 var in = new ObjectInputStream(inputStream)){
                return in.readObject();
                } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(this.fileName);
                 ObjectInputStream in = new ObjectInputStream(fis)) {
                return in.readObject();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void put(Object result){
        if (result instanceof List && this.listMaxSize > 0){
            try {
                List<Object> res = (List<Object>) result.getClass().getDeclaredConstructor().newInstance();
                Object o;
                for (int i = 0; i < this.listMaxSize; i++){
                    o = ((List<Object>) result).get(i);
                    res.add(o);
                }
                result = res;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassCastException e) {
                throw new RuntimeException(e);
            }
        }
        if (this.cacheType == CacheType.FILE){
            toFile(result);
        } else {
            memoryCache.put(this.key, result);
            if (this.cacheType == CacheType.BOTH){
                toFile(result);
            }
        }
    }

    public Object get(){
        Object res = null;
        if (this.cacheType == CacheType.MEMORY || this.cacheType == CacheType.BOTH){
            res = memoryCache.getOrDefault(this.key, null);
        }
        if (this.cacheType == CacheType.FILE || (res == null && this.cacheType == CacheType.BOTH)){
            res = fromFile();
        }
        return res;
    }

    public void setKey(Method method , Object[] args){
        List<Object> key = new ArrayList<>();
        key.add(method);
        if (this.identityBy.length == 0 ) {
            key.addAll(Arrays.asList(args));
        }
        else {
            Parameter[] pars = method.getParameters();
            for (Integer i : this.identityBy){
                if (i < pars.length){
                    key.add(pars[i]);
                } else{
                    throw new IllegalArgumentException("Значение нормера аргумента для кэширования больше количества аргументов метода!");
                }
            }
        }
        this.key = key;
    }
}