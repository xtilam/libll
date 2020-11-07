package com.dinz.library.common.updatemtm;

import com.dinz.library.common.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Getter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DinzeniLL
 */
@Getter
class MTMEntity {

    private MTMInfoEntity<?>[] mtmClasses = new MTMInfoEntity<?>[2];
    private final String tableNameDB;

    MTMEntity(Class<?> classFound, String fieldName) {
        // find relationship

        try {
            Class<?>[] classes = new Class<?>[2];

            // find join table anotation
            Field[] fieldsJoin = new Field[2];
            fieldsJoin[0] = classFound.getDeclaredField(fieldName);

            if (null == fieldsJoin[0]) throw new RuntimeException();

            {
                ManyToMany mtmAno = fieldsJoin[0].getAnnotation(ManyToMany.class);

                // get generic many to many field
                Type type = fieldsJoin[0].getGenericType();
                if (!(type instanceof ParameterizedType)) throw new RuntimeException();
                String className = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
                Class<?> classGeneric = Class.forName(className);

                if (mtmAno.mappedBy().length() == 0) {
                    classes[0] = classFound;
                    classes[1] = classGeneric;
                    for (Field field : classGeneric.getDeclaredFields()) {
                        mtmAno = field.getAnnotation(ManyToMany.class);
                        if (null != mtmAno && mtmAno.mappedBy().equals(fieldsJoin[0].getName())) {

                            // get generic many to many class
                            Type typeRef = field.getGenericType();
                            if (!(typeRef instanceof ParameterizedType)) throw new RuntimeException();
                            String classNameRef = ((ParameterizedType) typeRef).getActualTypeArguments()[0].getTypeName();
                            Class<?> classGenericRef = Class.forName(classNameRef);

                            if (classFound == classGenericRef) {
                                fieldsJoin[1] = field;
                                break;
                            }
                        }
                    }
                    Objects.requireNonNull(fieldsJoin[1]);
                } else {
                    classes[0] = classGeneric;
                    classes[1] = classFound;
                    fieldsJoin[1] = fieldsJoin[0];
                    fieldsJoin[0] = null;
                    fieldsJoin[0] = classGeneric.getDeclaredField(mtmAno.mappedBy());
                    Objects.requireNonNull(fieldsJoin[0]);
                }
            }

            JoinTable joinTable = fieldsJoin[0].getAnnotation(JoinTable.class);
            Objects.requireNonNull(joinTable);

            // init mtmInfoEntity
            {
                int index = 0;
                for (JoinColumn[] jcolsTemp : new JoinColumn[][]{
                    joinTable.joinColumns(),
                    joinTable.inverseJoinColumns()
                }) {
                    if (jcolsTemp.length == 0) throw new RuntimeException();
                    this.mtmClasses[index] = new MTMInfoEntity(classes[index], jcolsTemp[0].name(), jcolsTemp[0].referencedColumnName(), fieldsJoin[index].getName());
                    ++index;
                }
            }

            this.tableNameDB = joinTable.name();

        } catch (ClassNotFoundException | NoSuchFieldException | RuntimeException e) {
            Utils.debugConsole("error", e);
            throw new RuntimeException("Không thể tạo khởi tạo many to many giữa " + classFound + " với field" + fieldName, e);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MTMEntity) {
            final MTMEntity equalObj = (MTMEntity) obj;
            if (equalObj.mtmClasses[0].getClassEntity() == this.mtmClasses[0].getClassEntity())
                return equalObj.mtmClasses[1].getClassEntity() == this.mtmClasses[1].getClassEntity();
            else if (equalObj.mtmClasses[0].getClassEntity() == this.mtmClasses[1].getClassEntity())
                return equalObj.mtmClasses[1].getClassEntity() == this.mtmClasses[0].getClassEntity();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mtmClasses[0].getClassEntity(), this.mtmClasses[1].getClassEntity());
    }

    public <T> MTMInfoEntity<T> getMTMInfo(Class<T> entityClass) {
        if (this.mtmClasses[0].getClassEntity() == entityClass) return (MTMInfoEntity<T>) this.mtmClasses[0];
        else if (this.mtmClasses[1].getClassEntity() == entityClass) return (MTMInfoEntity<T>) this.mtmClasses[1];

        throw new RuntimeException("Not found Index entity class " + entityClass.getName());
    }
}
