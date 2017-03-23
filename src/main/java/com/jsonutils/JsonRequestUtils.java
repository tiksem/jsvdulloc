package com.jsonutils;

import com.utils.framework.collections.LazyLoadingList;

import java.util.*;

/**
 * Created by CM on 1/22/2015.
 */
public class JsonRequestUtils {

    public static <T> LazyLoadingList<T> getLazyLoadingList(final String url,
                                                          final Map<String, Object> params,
                                                          final String offsetParamName,
                                                          final Class<T> aClass) {

        return new JsonRequestNavigation<T>(url, params, offsetParamName, aClass).getLazyLoadingList();
    }

    public static <T> Iterator<T> getNavigationIterator(final String url,
                                                        final Map<String, Object> params,
                                                        final String offsetParamName,
                                                        final Class<T> aClass) {
        return new JsonRequestNavigation<T>(url, params, offsetParamName, aClass).getNavigationIterator();
    }

    public static <T> Iterable<T> getNavigationIterable(final String url,
                                                        final Map<String, Object> params,
                                                        final String offsetParamName,
                                                        final Class<T> aClass) {
        return new JsonRequestNavigation<T>(url, params, offsetParamName, aClass).getNavigationIterable();
    }
}
