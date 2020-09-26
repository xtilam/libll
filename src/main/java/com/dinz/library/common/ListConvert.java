package com.dinz.library.common;

import java.util.ArrayList;
import java.util.List;

public class ListConvert {
    public static <T, F> List<F> convert(List<T> list, IListConvertAction<T, F> action){
        if(null != list){
            List<F> listConvert = new ArrayList<>();
            for (T t : list) {
                listConvert.add(action.convertItem(t));
            }
            return listConvert;
        }
        return null;
    }
}