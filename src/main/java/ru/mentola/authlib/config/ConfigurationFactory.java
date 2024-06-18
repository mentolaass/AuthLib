package ru.mentola.authlib.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import ru.mentola.authlib.config.api.ConfigurationModel;
import ru.mentola.authlib.config.api.ConfigurationPath;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ConfigurationFactory {
    @Nullable
    public <T extends ConfigurationModel> T build(final FileConfiguration file, final Class<T> model) {
        try {
            final List<Field> fields = getAnnotatedFields(model);
            final List<Class<?>> typesFields = getTypesFields(fields);
            final Constructor<T> constructor = model.getConstructor(typesFields.toArray(Class<?>[]::new));
            return constructor.newInstance(configSectionsConvert(file, fields).toArray(Object[]::new));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Field> getAnnotatedFields(final Class<?> target) {
        return Arrays.stream(target.getDeclaredFields())
                .filter((field) -> field.isAnnotationPresent(ConfigurationPath.class))
                .toList();
    }

    private List<Class<?>> getTypesFields(final List<Field> fields) {
        final List<Class<?>> classes = new ArrayList<>();
        for (final Field field : fields)
            classes.add(field.getType());
        return classes;
    }

    private List<Object> configSectionsConvert(final FileConfiguration file, final List<Field> fields) {
        final List<Object> objects = new ArrayList<>();
        for (final Field field : fields) {
            final ConfigurationPath fieldCon = field.getAnnotation(ConfigurationPath.class);
            if (file.contains(fieldCon.path()))
                objects.add(getConfigObject(file, field.getType(), fieldCon.path()));
        }
        return objects;
    }

    @Nullable
    private Object getConfigObject(final FileConfiguration file, final Class<?> type, final String path) {
        if (type.equals(Integer.TYPE))
            return file.getInt(path);
        if (type.equals(String.class))
            return file.getString(path);
        if (type.equals(Long.TYPE))
            return file.getLong(path);
        if (type.equals(Double.TYPE))
            return file.getDouble(path);
        return null;
    }
}
