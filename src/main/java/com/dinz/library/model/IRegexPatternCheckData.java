/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.model;

import java.util.regex.Pattern;

/**
 *
 * @author dinzenida
 */
public interface IRegexPatternCheckData {

    Pattern EMAIL = Pattern.compile("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$");
    Pattern CODE = Pattern.compile("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$");
    Pattern NUMBER_STRING = Pattern.compile("^[0-9]*$");
}
