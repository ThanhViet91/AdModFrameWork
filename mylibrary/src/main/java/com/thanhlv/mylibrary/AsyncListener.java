package com.thanhlv.mylibrary;

public interface AsyncListener<T> {
    void onSuccess(T object);
    void onError(String e);
}
