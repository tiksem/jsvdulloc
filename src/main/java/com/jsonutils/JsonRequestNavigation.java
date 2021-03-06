package com.jsonutils;

import com.utils.framework.OnError;
import com.utils.framework.collections.LazyLoadingList;
import com.utils.framework.collections.OnLoadingFinished;
import com.utils.framework.collections.PageLoadingIterator;
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

    public LazyLoadingList<T> getLazyLoadingList() {
        return new LazyLoadingList<T>() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError) {
                elementsLoader.getElementsOfPage(this, onPageLoadingFinished, onError);
            }
        };
    }

    public Iterator<T> getNavigationIterator() {
        return new PageLoadingIterator<T>() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError) {
                elementsLoader.getElementsOfPage(this, onPageLoadingFinished, onError);
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
