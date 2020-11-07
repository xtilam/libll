package com.dinz.library;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public final class RegisterSqlFunction extends PostgreSQL10Dialect {

    public RegisterSqlFunction() {
        super();
        registerFunction("string_agg", new StandardSQLFunction("string_agg", StandardBasicTypes.STRING));
        registerFunction("string_to_array", new StandardSQLFunction("string_to_array", StandardBasicTypes.STRING));
        registerFunction("json_build_object", new StandardSQLFunction("json_build_object", StandardBasicTypes.STRING));
        registerFunction("json_agg", new StandardSQLFunction("json_agg", StandardBasicTypes.STRING));
        registerFunction("fix_json_agg_null", new StandardSQLFunction("fix_json_agg_null", StandardBasicTypes.STRING)); 
    }
}