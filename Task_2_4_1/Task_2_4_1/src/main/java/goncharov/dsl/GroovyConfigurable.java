package goncharov.dsl;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaProperty;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class GroovyConfigurable extends GroovyObjectSupport {
    public void postProcess() throws Exception {
        for (MetaProperty metaProperty : getMetaClass().getProperties()) {
            Object value = getProperty(metaProperty.getName());
            if (Collection.class.isAssignableFrom(metaProperty.getType()) &&
                    value instanceof Collection) {
                ParameterizedType collectionType = (ParameterizedType)
                        getClass().getDeclaredField(metaProperty.getName()).getGenericType();
                Class itemClass = (Class)collectionType.getActualTypeArguments()[0];
                if (GroovyConfigurable.class.isAssignableFrom(itemClass)) {
                    Collection collection = (Collection) value;
                    Collection newValue = collection.getClass().newInstance();
                    for (Object o : collection) {
                        if (o instanceof Closure) {
                            Object item = itemClass.getConstructor().newInstance();
                            ((Closure) o).setDelegate(item);
                            ((Closure) o).setResolveStrategy(Closure.DELEGATE_FIRST);
                            ((Closure) o).call();
                            ((GroovyConfigurable) item).postProcess(); // Рекурсивно запускаем для вложенных коллекций
                            newValue.add(item);
                        } else {
                            newValue.add(o);
                        }
                    }
                    setProperty(metaProperty.getName(), newValue);
                }
            }
        }
    }
}