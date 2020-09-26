package com.dinz.library.api.admin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.model.PropertyName;
import java.util.regex.Pattern;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.reflections.Reflections;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

public class APIUtils {

    static Map<String, String> fieldName = new HashMap<>();
    static final String FIELD_NOT_FOUND = "\"FIELD_NOT_FOUND\"";
    static String SQL_STATE_NULL_VALUE = "23502";
    static String NULL_VALUE_MESSAGE = " không hợp lệ";
    static String DUPLICATE_MESSAGE = " đã được sử dụng";
    static String SQL_STATE_DUPLICATE = "23505";
    static Pattern REGEX_RELATION = Pattern.compile("\\\".*?\\\"");

    static {
        Set<Class<?>> models = new Reflections("com.dinz.library").getTypesAnnotatedWith(Table.class);
        System.err.println("model size: " + models.size());
        for (final Class<?> model : models) {
            String tableName = model.getAnnotation(Table.class).name();
            for (final Field field : model.getDeclaredFields()) {
                PropertyName property = field.getAnnotation(PropertyName.class);
                if (null != property) {
                    String columnName = field.getAnnotation(Column.class).name();
                    fieldName.put(tableName + "_" + columnName, property.name());
                }
            }
        }

        fieldName.put("admin_admin_code_key", fieldName.get("admin_admin_code"));
        fieldName.put("admin_email_key", fieldName.get("admin_email"));
        fieldName.put("admin_identity_document_key", fieldName.get("admin_identity_document"));
        fieldName.put("admin_phone_key", fieldName.get("admin_phone"));
        fieldName.put("permission_permission_code_key", fieldName.get("permission_permission_code"));
    }

    public static String getErrorDataIntegrityViolation(DataIntegrityViolationException exception) {
        return getErrorDataIntegrityViolation((ConstraintViolationException) exception.getCause());
    }

    public static String getErrorDataIntegrityViolation(ConstraintViolationException ex) {
        String constraintName = ex.getConstraintName();
        String constraintProperty = fieldName.get(constraintName);
        String messageConcat = "";

        if (ex.getSQLState().equals(SQL_STATE_DUPLICATE)) {
            messageConcat = DUPLICATE_MESSAGE;
        } else if (ex.getSQLState().equals(SQL_STATE_NULL_VALUE)) {
            messageConcat = NULL_VALUE_MESSAGE;
        }

        if (null == constraintProperty) {
            String[] split = REGEX_RELATION.split(constraintName);
            if (split.length == 2) {
                constraintProperty = fieldName.get(split[1] + "_" + split[0]);
            }
            if (null == constraintProperty) {
                constraintProperty = FIELD_NOT_FOUND + ": " + constraintProperty + messageConcat;
            }
        }
        return constraintProperty + messageConcat;
    }

    public static String getErrorPropertyValue(final PropertyValueException ex) {
        try {
            Class<?> model = Class.forName(ex.getEntityName());
            String tableName = model.getAnnotation(Table.class).name();
            String column = model.getDeclaredField(ex.getPropertyName()).getAnnotation(Column.class).name();
            String err = fieldName.get(tableName + "_" + column);
            if (null == err) {
                err = FIELD_NOT_FOUND;
            }
            return err + NULL_VALUE_MESSAGE;
        } catch (final Exception e) {
            return ex.getEntityName() + " - " + ex.getPropertyName();
        }
    }

    public static APIResult getAPIResultPage(final Page<?> result) {
        if (result.getTotalPages() > result.getPageable().getPageNumber() || result.getTotalPages() == 0) {
            return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS)
                    .appendData("page", result.getPageable().getPageNumber() + 1)
                    .appendData("totalPage", result.getTotalPages())
                    .appendData("totalRecord", result.getTotalElements())
                    .appendData("offset", result.getPageable().getOffset()), result.getContent());
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.PAGE_OUT_INDEX).appendData("page", result.getPageable().getPageNumber() + 1)
                    .appendData("totalPage", result.getTotalPages()), "[]");
        }
    }
}
