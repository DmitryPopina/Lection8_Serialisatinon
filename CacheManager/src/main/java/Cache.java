import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    CacheType cacheType() default CacheType.MEMORY;
    String fileNamePrefix() default "";
    boolean zip() default false;
    int[] identityBy() default {};
    int listMaxSize() default 0;
}