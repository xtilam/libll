package com.dinz.library.common;

public interface IListConvertAction<T, F> {
    public F convertItem(T item);
}