package models.util;

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

    /**
     * Updates the values of this instance from a given object.
     * Will attempt to access all attributes of said object and
     * update this instance's attribute if the attribute is not null.
     *
     * Useful when de-serialising an incoming object using fromJson.
     *
     * @param objectToUpdateFrom      the incoming object used to update this instance.
     * @throws IllegalAccessException if an unreachable field is attempted to be accessed.
     */
    public void updateFromObject(BaseModel objectToUpdateFrom)
            throws IllegalAccessException {
        Class<?> classToUpdate = this.getClass();

        while (classToUpdate != null) {
            Field[] fields = classToUpdate.getDeclaredFields();

            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    if (field.get(objectToUpdateFrom) != null) {
                        field.set(this, field.get(objectToUpdateFrom));
                    }
                    field.setAccessible(accessible);
                }
            }

            classToUpdate = classToUpdate.getSuperclass();
        }
    }

}
