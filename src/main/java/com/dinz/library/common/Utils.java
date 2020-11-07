package com.dinz.library.common;

import com.dinz.library.function.ActionFunction;
import com.dinz.library.function.GetPageServiceAction;
import com.dinz.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static Long getRandomId() {
        return ThreadLocalRandom.current().nextLong(0, 9000000000000000L);
    }

    public static Method findMethod(Method[] methods, String methodName, Class<?> returnType, Class<?>... classes) throws NoSuchMethodException {
        Class<?>[] classParams = null;
        for (Method method : methods)
            if (method.getName().equals(methodName)
                    && (classes.length == (classParams = method.getParameterTypes()).length)
                    && (returnType.isAssignableFrom(method.getReturnType()))) {
                boolean isMatch = true;
                for (int i = 0; i < classParams.length; i++)
                    if (!classes[i].isAssignableFrom(classParams[i])) {
                        isMatch = false;
                        break;
                    }

                if (isMatch) return method;
            }
        throw new NoSuchMethodException();
    }

    public static void debugConsole(Object... objs) {
        StringBuilder st = new StringBuilder("");
        if (objs.length == 0)
            return;
        for (Object obj : objs)
            st.append(obj + " -> ");
        System.out.println(ANSI_YELLOW + st.substring(0, st.length() - 4).toString() + ANSI_RESET);
    }

    public static <T> Page<T> getLastPageService(GetPageServiceAction.CountAction countAction, GetPageServiceAction.GetPageAction<T> getPage, int limit) {
        int totalRecords = countAction.countAction();
        if (totalRecords > 0) {
            int maxPage = (totalRecords / limit) + (totalRecords % limit == 0 ? 0 : 1);
            return getPage.getPage(maxPage - 1, limit);
        } else {
            List<T> list = new ArrayList<>();
            return new PageImpl<>(list, PageRequest.of(0, limit), totalRecords);
        }
    }
}
