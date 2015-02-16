package com.jsonutils;

import com.utils.framework.collections.NavigationEntity;
import com.utils.framework.collections.OnLoadingFinished;
import com.utils.framework.io.IOExceptionListener;
import com.utils.framework.io.Network;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* Created by CM on 1/25/2015.
*/
class ElementsLoader<T> {
    private String url;
    private Map<String, Object> params;
    private String offsetParamName;
    private Class<T> aClass;
    private String responseKey = "response";
    private RequestExecutor requestExecutor;
    private IOExceptionListener ioExceptionListener;

    public ElementsLoader(String url, Map<String, Object> params, String offsetParamName, Class<T> aClass) {
        this.url = url;
        this.params = params;
        this.offsetParamName = offsetParamName;
        this.aClass = aClass;
    }

    public void getElementsOfPage(NavigationEntity<T> navigationEntity,
                                  OnLoadingFinished<T> onLoadingFinished) {
        int offset = navigationEntity.getLoadedElementsCount();
        params.put(offsetParamName, offset);
        List<T> result = null;
        try {
            String json;
            if (requestExecutor == null) {
                json = Network.executeGetRequest(url, params);
            } else {
                String urlCopy = Network.getUrl(url, params);
                json = requestExecutor.execute(urlCopy);
            }
            result = Json.readList(json, responseKey, aClass);
        } catch (IOException e) {
            if(ioExceptionListener != null){
                ioExceptionListener.onIOError(e);
            } else {
                throw new RuntimeException(e);
            }
        }

        if(result == null){
            onLoadingFinished.onLoadingFinished(Collections.<T>emptyList(), false);
        } else {
            onLoadingFinished.onLoadingFinished(result, result.isEmpty());
        }
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public String getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    public IOExceptionListener getIOExceptionListener() {
        return ioExceptionListener;
    }

    public void setIOExceptionListener(IOExceptionListener ioExceptionListener) {
        this.ioExceptionListener = ioExceptionListener;
    }
}
