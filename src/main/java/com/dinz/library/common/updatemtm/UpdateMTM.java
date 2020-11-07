/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.common.updatemtm;

import com.dinz.library.common.DoubleKey;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;

@Component
public class UpdateMTM {

    @PersistenceContext
    EntityManager em;

    public final Map<DoubleKey<Class, Class>, MTMEntity> retilationMTMFound = new HashMap<>();

    public UpdateMTM() {
        try {
            Set<Class<?>> entityClasses = new Reflections("com.dinz.library").getTypesAnnotatedWith(Entity.class);
            for (Class<?> entity : entityClasses)
                for (Field field : entity.getDeclaredFields()) {
                    ManyToMany mtm = field.getAnnotation(ManyToMany.class);
                    if (null == mtm) continue;
                    MTMEntity mtmEntity = new MTMEntity(entity, field.getName());
                    this.retilationMTMFound.put(new DoubleKey(mtmEntity.getMtmClasses()[0].getClassEntity(), mtmEntity.getMtmClasses()[1].getClassEntity()), mtmEntity);
                }
        } catch (Exception e) {
        }

    }

    public <FIRST_ENTITY, SECOND_ENTITY> void insert(Class<FIRST_ENTITY> firstEntity, Class<SECOND_ENTITY> secondEntity, FIRST_ENTITY obj) {
        this.update(firstEntity, secondEntity, obj, false);
    }

    public <FIRST_ENTITY, SECOND_ENTITY> void update(Class<FIRST_ENTITY> firstEntity, Class<SECOND_ENTITY> secondEntity, FIRST_ENTITY obj) {
        this.update(firstEntity, secondEntity, obj, true);
    }

    private <FIRST_ENTITY, SECOND_ENTITY> void update(Class<FIRST_ENTITY> firstEntity, Class<SECOND_ENTITY> secondEntity, FIRST_ENTITY obj, boolean updateMode) {
        MTMEntity mTMEntity = this.retilationMTMFound.get(new DoubleKey(firstEntity, secondEntity));

        if (null == mTMEntity)
            throw new RuntimeException("Không tìm thấy mối quan hệ many to many giữa 2 entity: " + firstEntity.getName() + " và " + secondEntity.getName());

        MTMInfoEntity<FIRST_ENTITY> firstMTMInfoEntity = mTMEntity.getMTMInfo(firstEntity);
        MTMInfoEntity<SECOND_ENTITY> secondMTMInfoEntity = mTMEntity.getMTMInfo(secondEntity);
        Object[] list = firstMTMInfoEntity.getList(obj);
        Object[] valuesUpdate = new Object[list.length];

        int index = 0;
        for (Object o : list) valuesUpdate[index++] = secondMTMInfoEntity.getValueUpdate(o, this.em);
        final Object firstJoinValue = firstMTMInfoEntity.getValueJoin(obj);


        if (updateMode) {
            String deleteSql = "delete from " + mTMEntity.getTableNameDB() + " where " + firstMTMInfoEntity.getJoinFieldDB() + " = ?";
            this.em.createNativeQuery(deleteSql).setParameter(1, firstJoinValue).executeUpdate();
        }

        String insertSql = "insert into " + mTMEntity.getTableNameDB()
                + "(" + firstMTMInfoEntity.getJoinFieldDB() + "," + secondMTMInfoEntity.getJoinFieldDB() + ")"
                + " values(?, ?)";
        for (Object o : valuesUpdate)
            this.em.createNativeQuery(insertSql)
                    .setParameter(1, firstJoinValue)
                    .setParameter(2, o)
                    .executeUpdate();
    }
}
