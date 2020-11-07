/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.common;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author DinzeniLL
 */
@AllArgsConstructor
@Data
public class DoubleKey<K, V> implements Serializable {

    private final K key1;
    private final V key2;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleKey) {
            DoubleKey keyEqual = (DoubleKey) obj;
            if ((Objects.equals(this.key1, keyEqual.key1) && Objects.equals(this.key2, keyEqual.key2))
                    || (Objects.equals(this.key1, keyEqual.key2) && Objects.equals(this.key2, keyEqual.key1)))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key1.hashCode() + key2.hashCode();
    }

    @Override
    public String toString() {
        return "DoubleKey{" + "key1=" + key1 + ", key2=" + key2 + '}';
    }

}
