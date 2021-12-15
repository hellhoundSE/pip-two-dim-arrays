import jdk.nashorn.internal.ir.annotations.Ignore;
import sun.reflect.ReflectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public final class GenericTestFactory {

    public static <T> T getInstance(Class clazz) {

        T instance = null;

        try {
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            Constructor<?> objDef = Object.class.getDeclaredConstructor();
            Constructor<?> intConstr = rf.newConstructorForSerialization(clazz, objDef);
            instance = (T) intConstr.newInstance();
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return instance;
    }

    public static <T> List<T> getInstance(Class clazz, Object[] parameters) {

        List<T> createdObjects = new ArrayList<>();
        try {
            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Constructor<?> constructor : clazz.getConstructors()) {

                    List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                    List<Class> methodParameters = Arrays.asList(constructor.getParameterTypes());

                    if (sameParametersSequence(permutationClasses, methodParameters)) {
                        T instance = (T) constructor.newInstance(currentPermutation.toArray());
                        createdObjects.add(instance);
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return createdObjects;
    }

    public static <T> List<T> getInstance(Class clazz, Object[] parameters, boolean isPrimitiveTypes) {

        List<T> createdObjects = new ArrayList<>();
        try {
            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Constructor<?> constructor : clazz.getConstructors()) {

                    List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                    List<Class> methodParameters = Arrays.asList(constructor.getParameterTypes());

                    if (isPrimitiveTypes) {
                        for (int i = 0; i < permutationClasses.size(); i++)
                            permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                        for (int i = 0; i < methodParameters.size(); i++)
                            methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                    }

                    if (sameParametersSequence(permutationClasses, methodParameters)) {
                        T instance = (T) constructor.newInstance(currentPermutation.toArray());
                        createdObjects.add(instance);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdObjects;
    }

    public static <T> List<T> getInstance(Class clazz, Object[] parameters, boolean[] isPrimitiveTypes) {

        List<T> createdObjects = new ArrayList<>();
        try {
            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Constructor<?> constructor : clazz.getConstructors()) {

                    List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                    List<Class> methodParameters = Arrays.asList(constructor.getParameterTypes());

                    for (int i = 0; i < isPrimitiveTypes.length; i++) {
                        if (isPrimitiveTypes[i]) {
                            permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                            methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                        }
                    }

                    if (sameParametersSequence(permutationClasses, methodParameters)) {
                        T instance = (T) constructor.newInstance(currentPermutation.toArray());
                        createdObjects.add(instance);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdObjects;
    }

    public static <T> T getProperObject(List<T> objects, String[] fieldNames, Object[] results){

        try {
            for (T obj : objects) {
                boolean isCorrect = true;
                for (int i = 0; i < fieldNames.length; i++) {
                    Object realValue = GenericTestFactory.getField(obj, fieldNames[i]);
                    if (!Objects.equals(realValue,results[i])){
                        isCorrect = false;
                    }
                }
                if(isCorrect)
                    return obj;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public static void testFieldExists(Class clazz, String field) {

        try {
            assertNotNull(clazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }

    public static void testFieldType(Class clazz, String fieldName, Class type) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            assertEquals(f.getType().getName(), type.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }

    public static void testFieldType(Class clazz, String fieldName, Class type, boolean isPrimitiveType) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            if (isPrimitiveType)
                type = convertToPrimitiveType(type);
            assertEquals(f.getType().getName(), type.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }

    public static void testFieldModifier(Class clazz, String fieldName, int modifier) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            switch (modifier) {
                case Modifier.PRIVATE:
                    assertTrue(Modifier.isPrivate(f.getModifiers()));
                    break;
                case Modifier.PUBLIC:
                    assertTrue(Modifier.isPublic(f.getModifiers()));
                    break;
                case Modifier.PROTECTED:
                    assertTrue(Modifier.isProtected(f.getModifiers()));
                    break;
            }
            return;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        fail();
    }

    public static void testFieldHasNoModifiers(Class clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            if (!Modifier.isPublic(f.getModifiers()) && !Modifier.isProtected(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers())) {
                assertTrue(!Modifier.isPublic(f.getModifiers()) && !Modifier.isProtected(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers()));
                return;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        fail();
    }

    public static void testMethodModifier(Class clazz, String methodName, int modifier) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (Objects.equals(m.getName(), (methodName))) {
                    switch (modifier) {
                        case Modifier.PRIVATE:
                            assertTrue(Modifier.isPrivate(m.getModifiers()));
                            break;
                        case Modifier.PUBLIC:
                            assertTrue(Modifier.isPublic(m.getModifiers()));
                            break;
                        case Modifier.PROTECTED:
                            assertTrue(Modifier.isProtected(m.getModifiers()));
                            break;
                    }
                }
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        fail();
    }

    public static void testMethodHasNoModifiers(Class clazz, String methodName) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (Objects.equals(m.getName(), (methodName))) {
                    if (!Modifier.isPublic(m.getModifiers()) && !Modifier.isProtected(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers())) {
                        assertTrue(!Modifier.isPublic(m.getModifiers()) && !Modifier.isProtected(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()));
                        return;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fail();
    }

    public static <T, F> T testGetter(F instance, String fieldName) {

        Field field = null;

        try {
            field = instance.getClass().getDeclaredField(fieldName);

            if (Modifier.isPublic(field.getModifiers())) {
                return (T) field.get(instance);
            }

            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            if (field.getGenericType().toString().equals(Boolean.class.toString()) || "boolean".equals(field.getGenericType().toString())) {
                getterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            }
            Method method = instance.getClass().getDeclaredMethod(getterName);
            return (T) method.invoke(instance);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return null;
    }

    public static <T, F> void testSetter(F instance, String fieldName, T expectedValue) {

        Field field = null;

        try {
            field = instance.getClass().getDeclaredField(fieldName);

            if (Modifier.isPublic(field.getModifiers()))
                assertTrue(Modifier.isPublic(field.getModifiers()));

            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Class expectedClass = expectedValue.getClass();

            Class c = expectedValue.getClass();
            Method method = instance.getClass().getDeclaredMethod(setterName, expectedClass);
            method.invoke(instance, expectedValue);

            field.setAccessible(true);
            boolean result = field.get(instance).equals(expectedValue);
            field.setAccessible(false);

            assertTrue(result);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public static <T, F> void testSetter(F instance, String fieldName, T expectedValue, boolean isPrimitiveType) {

        Field field = null;

        try {
            field = instance.getClass().getDeclaredField(fieldName);

            if (Modifier.isPublic(field.getModifiers()))
                assertTrue(Modifier.isPublic(field.getModifiers()));

            else {
                String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                Class expectedClass = expectedValue.getClass();

                if (isPrimitiveType)
                    expectedClass = convertToPrimitiveType(expectedValue.getClass());

                Class c = expectedValue.getClass();
                Method method = instance.getClass().getDeclaredMethod(setterName, expectedClass);
                method.invoke(instance, expectedValue);

                field.setAccessible(true);
                boolean result = field.get(instance).equals(expectedValue);
                field.setAccessible(false);

                assertTrue(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public static <T, F> F getField(T instance, String fieldName) {
        Field field = null;

        try {
            field = instance.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            F value = (F) field.get(instance);
            field.setAccessible(false);
            return value;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void testSystemOutputFromMethod(T instance, String methodName, String expectedOutput, Object[] parameters) {

        PrintStream originalOut = System.out;

        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {


                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            System.setOut(new PrintStream(bos));

                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                method.invoke(instance, currentPermutation.toArray());

                            String realOutput = bos.toString().replaceAll("\\s+", "").toLowerCase();
                            expectedOutput = expectedOutput.replaceAll("\\s+", "").toLowerCase();
                            bos.close();

                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            System.setOut(originalOut);
        }
        fail();

    }

    public static <T> void testSystemOutputFromMethod(T instance, String methodName, String expectedOutput, Object[] parameters, boolean isPrimitiveTypes) {

        PrintStream originalOut = System.out;


        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (isPrimitiveTypes) {
                            for (int i = 0; i < permutationClasses.size(); i++)
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                            for (int i = 0; i < methodParameters.size(); i++)
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                        }

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            System.setOut(new PrintStream(bos));

                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                method.invoke(instance, currentPermutation.toArray());

                            String realOutput = bos.toString().replaceAll("\\s+", "").toLowerCase();
                            expectedOutput = expectedOutput.replaceAll("\\s+", "").toLowerCase();
                            bos.close();

                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            System.setOut(originalOut);
        }
        fail();
    }

    public static <T> void testSystemOutputFromMethod(T instance, String methodName, String expectedOutput, Object[] parameters, boolean[] isPrimitiveTypes) {

        PrintStream originalOut = System.out;

        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        for (int i = 0; i < isPrimitiveTypes.length; i++) {
                            if (isPrimitiveTypes[i]) {
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                            }
                        }

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            System.setOut(new PrintStream(bos));

                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                method.invoke(instance, currentPermutation.toArray());

                            String realOutput = bos.toString().replaceAll("\\s+", "").toLowerCase();
                            expectedOutput = expectedOutput.replaceAll("\\s+", "").toLowerCase();
                            bos.close();
                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            System.setOut(originalOut);
        }
        fail();
    }

    public static <T> void testMethod(T instance, String methodName, Object expectedOutput, Object[] parameters) {

        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            Object realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = method.invoke(instance, currentPermutation.toArray());

                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        fail();
    }

    public static <T> void testMethod(T instance, String methodName, Object expectedOutput, Object[] parameters, boolean isPrimitiveTypes) {

        if (parameters == null)
            parameters = new Object[0];


        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {


                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (isPrimitiveTypes) {
                            for (int i = 0; i < permutationClasses.size(); i++)
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                            for (int i = 0; i < methodParameters.size(); i++)
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                        }

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            Object realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = method.invoke(instance, currentPermutation.toArray());


                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        fail();
    }

    public static <T> void testMethod(T instance, String methodName, Object expectedOutput, Object[] parameters, boolean[] isPrimitiveTypes) {

        if (parameters == null)
            parameters = new Object[0];


        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {


                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        for (int i = 0; i < isPrimitiveTypes.length; i++) {
                            if (isPrimitiveTypes[i]) {
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                            }
                        }


                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            Object realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = method.invoke(instance, currentPermutation.toArray());


                            if (expectedOutput.equals(realOutput)) {
                                assertEquals(expectedOutput, realOutput);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        fail();
    }

    public static <T, F> List<F> invokeMethod(T instance, String methodName, Object[] parameters) {

        List<F> returnList = new ArrayList<>();

        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            F realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());

                            returnList.add(realOutput);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return returnList;
    }

    public static <T, F> List<F> invokeMethod(T instance, String methodName, Object[] parameters, boolean isPrimitiveTypes) {

        List<F> returnList = new ArrayList<>();

        if (parameters == null)
            parameters = new Object[0];

        try {
            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        if (isPrimitiveTypes) {
                            for (int i = 0; i < permutationClasses.size(); i++)
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                            for (int i = 0; i < methodParameters.size(); i++)
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                        }

                        if (sameParametersSequence(permutationClasses, methodParameters)) {
                            F realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());

                            returnList.add(realOutput);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return returnList;
    }

    public static <T, F> List<F> invokeMethod(T instance, String methodName, Class[] requiredParameters, Object[] parameters, boolean[] isPrimitiveTypes) {

        List<F> returnList = new ArrayList<>();

        if (parameters == null)
            parameters = new Object[0];

        try {

            ArrayList<ArrayList<Object>> allPermutations = permute(parameters);

            for (List<Object> currentPermutation : allPermutations) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {

                        List<Class> permutationClasses = currentPermutation.stream().map(f -> f.getClass()).collect(Collectors.toList());
                        List<Class> methodParameters = Arrays.asList(method.getParameterTypes());

                        for (int i = 0; i < isPrimitiveTypes.length; i++) {
                            if (isPrimitiveTypes[i]) {
                                permutationClasses.set(i, convertToPrimitiveType(permutationClasses.get(i)));
                                methodParameters.set(i, convertToPrimitiveType(methodParameters.get(i)));
                            }
                        }

                        if (sameParametersSequence(permutationClasses, methodParameters)) {

                            F realOutput;
                            if (!Modifier.isPublic(method.getModifiers())) {
                                method.setAccessible(true);
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());
                                method.setAccessible(false);
                            } else
                                realOutput = (F) method.invoke(instance, currentPermutation.toArray());

                            returnList.add(realOutput);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return returnList;
    }

    private static Class convertToPrimitiveType(Class clazz) {
        if (clazz.equals(Boolean.class)) return boolean.class;
        if (clazz.equals(Integer.class)) return int.class;
        if (clazz.equals(Double.class)) return double.class;
        if (clazz.equals(Float.class)) return float.class;
        if (clazz.equals(Short.class)) return short.class;
        if (clazz.equals(Long.class)) return long.class;
        if (clazz.equals(Byte.class)) return byte.class;
        if (clazz.equals(Character.class)) return char.class;
        if (clazz.equals(Character.class)) return char.class;
        return clazz;
    }

    public static ArrayList<ArrayList<Object>> permute(Object[] num) {
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();

        //start from an empty list
        result.add(new ArrayList<Object>());

        for (int i = 0; i < num.length; i++) {
            ArrayList<ArrayList<Object>> current = new ArrayList<ArrayList<Object>>();

            for (ArrayList<Object> l : result) {
                for (int j = 0; j < l.size() + 1; j++) {
                    l.add(j, num[i]);
                    ArrayList<Object> temp = new ArrayList<Object>(l);
                    current.add(temp);
                    l.remove(j);
                }
            }

            result = new ArrayList<ArrayList<Object>>(current);
        }

        return result;
    }

    public static boolean sameParametersSequence(List<Class> seq1, List<Class> seq2) {
        if (seq1.size() != seq2.size())
            return false;
        for (int i = 0; i < seq1.size(); i++) {
            if (!seq1.get(i).getName().equals(seq2.get(i).getName()))
                return false;
        }
        return true;
    }

}





