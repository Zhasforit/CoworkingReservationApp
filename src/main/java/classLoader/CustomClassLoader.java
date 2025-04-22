package classLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

    private final String basePath;

    public CustomClassLoader(String basePath) {
        this.basePath = basePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        Path fullPath = Paths.get(basePath, path);

        try {
            byte[] classBytes = Files.readAllBytes(fullPath);

            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }

    public static void main(String[] args) throws Exception {
        String basePath = "build/classes/java/main";
        CustomClassLoader loader = new CustomClassLoader(basePath);

        Class<?> loadedClass = loader.loadClass("classLoader.Workspace");

        Object instance = loadedClass.newInstance();

        System.out.println("Class loaded by: " + instance.getClass().getClassLoader());
    }
}