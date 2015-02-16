package com.jsonutils;

import com.utils.framework.collections.NavigationEntity;
import com.utils.framework.collections.NavigationIterator;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.collections.OnLoadingFinished;
import com.utils.framework.io.IOExceptionListener;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by CM on 1/25/2015.
 */
public class JsonRequestNavigation<T> {
    private final ElementsLoader<T> elementsLoader;

    public JsonRequestNavigation(String url, Map<String, Object> params, String offsetParamName, Class<T> aClass) {
        elementsLoader = new ElementsLoader<T>(url, params, offsetParamName, aClass);
    }

    public NavigationList<T> getNavigationList() {
        return new NavigationList<T>() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished) {
                elementsLoader.getElementsOfPage(this, onPageLoadingFinished);
            }
        };
    }

    public Iterator<T> getNavigationIterator() {
        return new NavigationIterator<T>() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished) {
                elementsLoader.getElementsOfPage(this, onPageLoadingFinished);
            }
        };
    }

    public Iterable<T> getNavigationIterable() {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return getNavigationIterator();
            }
        };
    }

    public RequestExecutor getRequestExecutor() {
        return elementsLoader.getRequestExecutor();
    }

    public void setRequestExecutor(RequestExecutor requestExecutor) {
        elementsLoader.setRequestExecutor(requestExecutor);
    }

    public String getResponseKey() {
        return elementsLoader.getResponseKey();
    }

    public void setResponseKey(String responseKey) {
        elementsLoader.setResponseKey(responseKey);
    }

    public IOExceptionListener getIOExceptionListener() {
        return elementsLoader.getIOExceptionListener();
    }

    public void setIOExceptionListener(IOExceptionListener ioExceptionListener) {
        elementsLoader.setIOExceptionListener(ioExceptionListener);
    }
}
