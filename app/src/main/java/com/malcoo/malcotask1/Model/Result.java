package com.malcoo.malcotask1.Model;

public class Result<T> {
    Boolean isLoading;
    T data;
    String error;
    boolean success;

    public Result(Boolean isLoading, T data, String error, boolean success) {
        this.isLoading = isLoading;
        this.data = data;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> SUCCESS(T data){
        return new Result<T>(false,data,null,true);
    }

    public static <T>Result<T> LOADING(){
        return new Result<T>(true,null,null,false);
    }
    public static <T >Result<T> ERROR(String error){
        return new Result<T>(false,null,error,false);
    }

    public Boolean getLoading() {
        return isLoading;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
