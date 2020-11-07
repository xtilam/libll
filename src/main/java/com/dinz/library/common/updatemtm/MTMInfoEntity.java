/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.common.updatemtm;

import com.dinz.library.common.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;

import com.dinz.library.model.PropertyName;
import lombok.AllArgsConstructor;

/**
 * @author DinzeniLL
 */
@AllArgsConstructor
public class MTMInfoEntity<T> {

    private final Class<T> classEntity;
    private final String joinFieldDB;
    private final String joinFieldReferenceDB;
    private final String joinFieldEntity;
    private RuntimeException fieldNullException;
    private final String idFieldEntityClass;
    private final Method getIdMethod;
    private final String excludeCondition;
    private final Method getListMethod;
    private final Method getJoinValueMethod;

    public MTMInfoEntity(Class<T> classEntity, String joinFieldDB, String joinFieldReferenceDB, String mtmFieldEntity) {
        try {
            String idFieldEntityClass = null;
            Class superClass = classEntity.getSuperclass();
            if (superClass.getAnnotation(MappedSuperclass.class) == null) superClass = null;

            Class<?> classOfIdField = null;
            String joinFieldName = null;

            // find condition query
            ExcludeConditionMTM excludeConditionMTM = classEntity.getAnnotation(ExcludeConditionMTM.class);
            if (null == excludeConditionMTM || excludeConditionMTM.condition().length() == 0)
                this.excludeCondition = "";
            else
                this.excludeCondition = "and " + excludeConditionMTM.condition();

            // find id field of entity => Category.id
            for (Field field : classEntity.getDeclaredFields()) {

                if (field.getAnnotation(javax.persistence.Id.class) != null) {
                    classOfIdField = field.getType();
                    Column col = field.getAnnotation(javax.persistence.Column.class);

                    if (null == col) idFieldEntityClass = field.getName();
                    else idFieldEntityClass = col.name();

                    break;
                }
            }
            if (idFieldEntityClass == null && superClass != null)
                for (Field field : superClass.getDeclaredFields()) {

                    if (field.getAnnotation(javax.persistence.Id.class) != null) {
                        classOfIdField = field.getType();
                        Column col = field.getAnnotation(javax.persistence.Column.class);

                        if (null == col) idFieldEntityClass = field.getName();
                        else idFieldEntityClass = col.name();

                        break;
                    }
                }

            if (idFieldEntityClass == null)
                throw new RuntimeException("Không tìm thấy field id của entity: " + classEntity.getName());

            // find join field name => Category.categoryCode
            for (Field field : classEntity.getDeclaredFields()) {
                Column colAno = field.getAnnotation(javax.persistence.Column.class);
                if (colAno != null && colAno.name().equals(joinFieldDB)) {
                    joinFieldName = field.getName();
                    final PropertyName propertyName = field.getAnnotation(PropertyName.class);
                    if (null != propertyName)
                        this.fieldNullException = new RuntimeException(propertyName.name() + " không hợp lệ");
                    else
                        this.fieldNullException = new RuntimeException(joinFieldName + " không hợp lệ");
                    break;
                }

                if (field.getName().equals(joinFieldDB))
                    joinFieldName = joinFieldDB;
            }

            String getterValueJoinName = "get"
                    + joinFieldName.substring(0, 1).toUpperCase() + joinFieldName.substring(1);
            this.getJoinValueMethod = Objects.requireNonNull(
                    Utils.findMethod(classEntity.getDeclaredMethods(), getterValueJoinName, Object.class),
                    "Không tìm thấy method getList: " + classEntity.getName() + "->" + joinFieldName
            );

            // find getterId method => Category.getId  
            String getterIdMethodName = "get"
                    + idFieldEntityClass.substring(0, 1).toUpperCase() + idFieldEntityClass.substring(1);

            {
                Method getIdMethod = null;
                try {
                    getIdMethod = Utils.findMethod(classEntity.getDeclaredMethods(), getterIdMethodName, classOfIdField);
                } catch (Exception e) {
                }
                if (null == getIdMethod && null != superClass)
                    getIdMethod = Utils.findMethod(superClass.getDeclaredMethods(), getterIdMethodName, classOfIdField);
                if (null == getIdMethod)
                    throw new RuntimeException("Không tìm thấy method getId: " + classEntity.getName());
                this.getIdMethod = getIdMethod;
            }

            // find getList => Category.getBooks
            String getListMethodName = "get"
                    + mtmFieldEntity.substring(0, 1).toUpperCase() + mtmFieldEntity.substring(1);

            this.getListMethod = Objects.requireNonNull(
                    Utils.findMethod(classEntity.getDeclaredMethods(), getListMethodName, Object.class),
                    "Không tìm thấy method getList: " + classEntity.getName() + "->" + getListMethodName
            );

            this.joinFieldEntity = joinFieldName;
            this.idFieldEntityClass = idFieldEntityClass;
            this.classEntity = classEntity;
            this.joinFieldDB = joinFieldDB;
            this.joinFieldReferenceDB = joinFieldReferenceDB.length() == 0 ? idFieldEntityClass : joinFieldReferenceDB;

        } catch (NoSuchMethodException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi khởi tạo MTMInfo", e);
        }
    }

    public Class<T> getClassEntity() {
        return classEntity;
    }

    public String getJoinFieldDB() {
        return joinFieldDB;
    }

    public String getJoinFieldReferenceDB() {
        return joinFieldReferenceDB;
    }

    public String getIdFieldEntityClass() {
        return idFieldEntityClass;
    }

    public String getListMethod() {
        return this.getListMethod.getName();
    }

    public Object[] getList(Object obj) {
        try {
            Object invoke = this.getListMethod.invoke(obj);
            if (invoke.getClass().isArray())
                return (Object[]) invoke;
            else if (Collection.class.isAssignableFrom(invoke.getClass()))
                return ((Collection) invoke).toArray();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }
        return new Object[]{};
    }

    public Object getValueJoin(Object obj) {
        Object output = null;
        try {
            output = this.getJoinValueMethod.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("cannot read join value");
        }
        if (null == output) throw this.fieldNullException;
        return output;
    }

    public Object getValueUpdate(Object obj, EntityManager em) {
        try {
            final String sql = "select " + this.joinFieldEntity + " from " + this.classEntity.getSimpleName() + " where " + this.idFieldEntityClass + " = ?1 " + this.excludeCondition;
            Utils.debugConsole("sql string", sql);
            return em.createQuery(sql).setParameter(1, this.getIdMethod.invoke(obj)).getSingleResult();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("cannot get id from prams");
        }
    }
}
