/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.dinz.library.LibApplication;
import com.dinz.library.common.qano.ConfigType;
import com.dinz.library.common.qano.ResultConfig;
import com.dinz.library.exception.QueryRepoException;
import com.dinz.library.repository.AdminRepository;

/**
 *
 * @author DinzeniLL
 */
@Component
public class QueryUtils {

    @Autowired
    AdminRepository adminRepo;
    static final Pattern findSelect = Pattern.compile("[sS][eE][lL][eE][cC][tT]\\s*(.*)\\s*?[fF][rR][oO][mM]");
    static final Pattern[] findFileld = new Pattern[] { Pattern.compile(".*?as\\s*\\\"*([a-zA-Z0-9]+)"),
            Pattern.compile(".*?\\.([a-zA-Z0-9]+)") };

    @Transactional
    public <T, R> List<R> getListFromRepo(Class<?> repoClass, String methodName, Class<R> resultClass,
            Object... params) {
        // lấy class của params để tìm method
        Class<?>[] classes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            classes[i] = params[i].getClass();
        }
        Method methodInRepo = null;
        try {
            methodInRepo = repoClass.getMethod(methodName, classes);
        } catch (Exception e) {
            throw new QueryRepoException.NotFoundMethodRepository(repoClass, methodName);
        }

        // constructor để init đối tượng
        Constructor<R> constructorResult = null;
        Method[] setterMethodsOfResultObject = null;

        try {
            constructorResult = resultClass.getConstructor();
        } catch (Exception ex) {
            throw new QueryRepoException.NotFoundConstructResult(resultClass);
        }

        // Repo phải trả về Stream
        if (!methodInRepo.getGenericReturnType().getTypeName().equals("java.util.stream.Stream<java.lang.Object[]>")) {
            throw new QueryRepoException.ReturnTypeNotExpected(repoClass, methodName);
        }

        ResultConfig configs = methodInRepo.getAnnotation(ResultConfig.class);
        // Lấy method setter và constructor ưu tiên nếu có config
        if (configs != null) {
            for (ConfigType config : configs.config()) {
                if (config.targetClass().isAssignableFrom(resultClass)) {
                    setterMethodsOfResultObject = this.findSetterMethod(config.fields(), resultClass);
                }
            }
        } else {
            Query query = methodInRepo.getAnnotation(Query.class);
            if (query != null) {
                setterMethodsOfResultObject = this.findResultQuery(query.value(), resultClass);
            } else {
                throw new QueryRepoException.NotFoundQuery(repoClass, methodName);
            }
        }

        try {
            return this.getListResultMapping(constructorResult, setterMethodsOfResultObject,
                    (Stream<Object[]>) methodInRepo.invoke(LibApplication.getContext().getBean(repoClass), params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public <T, R> List<R> queryListResult(javax.persistence.Query query, Class<R> resultClass) {
        Constructor<R> constructor = null;
        try {
            constructor = resultClass.getConstructor();
        } catch (Exception e) {
            throw new QueryRepoException.NotFoundConstructResult(resultClass);
        }

        Method[] setterMethods = findResultQuery(query.unwrap(org.hibernate.query.Query.class).getQueryString(),
                resultClass);
        if (setterMethods.length == 0) {
            throw new QueryRepoException.NotFoundMethodSetParam(null, resultClass);
        }

        try {
            Stream stream = query.getResultStream();
            if (setterMethods.length > 1) {
                return this.getListResultMapping(constructor, setterMethods, (Stream<Object[]>) stream);
            } else {
                List<R> result = new ArrayList<>();
                Iterator<?> iter = stream.iterator();
                R obj;
                while (iter.hasNext()) {
                    obj = constructor.newInstance();
                    setterMethods[0].invoke(obj, iter.next());
                    result.add(obj);
                }
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public <R> R queryNativeListResult(javax.persistence.Query query, Class<R> resultClass) {
        Constructor<R> constructor = null;
        try {
            constructor = resultClass.getConstructor();
        } catch (Exception e) {
            throw new QueryRepoException.NotFoundConstructResult(resultClass);
        }

        Method[] setterMethods = findResultQuery(query.unwrap(org.hibernate.query.Query.class).getQueryString(),
                resultClass);
        if (setterMethods.length == 0) {
            throw new QueryRepoException.NotFoundMethodSetParam(null, resultClass);
        }

        try {
            Object rs = query.getSingleResult();
            if (setterMethods.length > 1) {
                return this.getResultMapping(constructor, setterMethods, (Object[]) rs);
            } else {
                R obj;
                obj = constructor.newInstance();
                setterMethods[0].invoke(obj, rs);
                return obj;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public <T, R> R getSingleFromRepo(Class<?> repoClass, String methodName, Class<R> resultClass, Object... params) {
        // lấy class của params để tìm method
        Class[] classes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            classes[i] = params[i].getClass();
        }
        Method methodInRepo = null;
        try {
            methodInRepo = repoClass.getMethod(methodName, classes);
        } catch (Exception e) {
            throw new QueryRepoException.NotFoundMethodRepository(repoClass, methodName);
        }

        // constructor để init đối tượng
        Constructor<R> constructorResult = null;
        Method[] setterMethodsOfResultObject = null;

        try {
            constructorResult = resultClass.getConstructor();
        } catch (Exception ex) {
            throw new QueryRepoException.NotFoundConstructResult(resultClass);
        }

        ResultConfig configs = methodInRepo.getAnnotation(ResultConfig.class);
        // Lấy method setter và constructor ưu tiên nếu có config
        if (configs != null) {
            for (ConfigType config : configs.config()) {
                if (config.targetClass().isAssignableFrom(resultClass)) {
                    setterMethodsOfResultObject = this.findSetterMethod(config.fields(), resultClass);
                }
            }
        } else {
            Query query = methodInRepo.getAnnotation(Query.class);
            if (query != null) {
                setterMethodsOfResultObject = this.findResultQuery(query.value(), resultClass);
            } else {
                throw new QueryRepoException.NotFoundQuery(repoClass, methodName);
            }
        }

        try {
            return this.getResultMapping(constructorResult, setterMethodsOfResultObject,
                    (Object[]) methodInRepo.invoke(LibApplication.getContext().getBean(repoClass), params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> getListResultMapping(Constructor<T> constructor, Method[] resultMethods,
            Stream<Object[]> resultStream)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        java.util.Iterator<Object[]> iter = resultStream.iterator();
        List<T> resultQuery = new ArrayList<>();
        while (iter.hasNext()) {
            resultQuery.add(this.getResultMapping(constructor, resultMethods, iter.next()));
        }
        return resultQuery;
    }

    private <T> T getResultMapping(Constructor<T> constructor, Method[] resultMethods, Object[] data)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        T obj = constructor.newInstance();
        obj = constructor.newInstance();
        for (int i = 0; i < data.length; i++) {
            resultMethods[i].invoke(obj, data[i]);
        }
        return obj;
    }

    private Method[] findResultQuery(String query, Class<?> resultClass) {
        try {
            String fieldSelect = null;
            Matcher matcher = findSelect.matcher(query);
            if (matcher.find()) {
                fieldSelect = matcher.group(1);
            } else {
                throw new QueryRepoException("Not found select field");
            }
            String[] fields = fieldSelect.split("\\s*\\,\\s*");
            for (int i = 0; i < fields.length; i++) {
                matcher = findFileld[0].matcher(fields[i]);
                if (matcher.find()) {
                    fields[i] = matcher.group(1);
                    continue;
                }
                matcher = findFileld[1].matcher(fields[i]);
                if (matcher.find()) {
                    fields[i] = matcher.group(1);
                }
            }

            return this.findSetterMethod(fields, resultClass);
        } catch (QueryRepoException e) {
            throw e;
        }
    }

    private Method[] findSetterMethod(String[] fields, Class<?> resultClass) {
        Method[] methodResult = new Method[fields.length];
        Method[] methodSearch = resultClass.getMethods();

        // Field String to Method
        String method = null;
        int length = fields.length;
        try {
            for (int i = 0; i < length; i++) {
                method = fields[i];
                method = "set" + method.substring(0, 1).toUpperCase() + method.substring(1);
                methodResult[i] = this.findMethod(methodSearch, method, Object.class);
            }
        } catch (Exception e) {
            throw new QueryRepoException(
                    "Not found method \"" + method + "\" in class \"" + resultClass.getName() + "\"");
        }

        return methodResult;
    }

    private Method findMethod(Method[] methods, String methodName, Class<?>... classes) throws NoSuchMethodException {
        Class<?>[] classParams = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                    && (classes.length == (classParams = method.getParameterTypes()).length)) {
                boolean isMatch = true;
                for (int i = 0; i < classParams.length; i++) {
                    if (!classes[i].isAssignableFrom(classParams[i])) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    return method;
                }
            }
        }
        throw new NoSuchMethodException();
    }

}
