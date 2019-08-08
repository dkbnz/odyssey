package models;

import io.ebean.Model;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@MappedSuperclass
public class
BaseModel extends Model {

    @Id
    public Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateFromObject(BaseModel objToUse)
            throws IllegalAccessException {
        Class<?> clazz = this.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    if (field.get(objToUse) != null) {
                        field.set(this, field.get(objToUse));
                    }
                    field.setAccessible(accessible);
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

}
