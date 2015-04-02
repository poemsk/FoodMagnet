package com.poepoemyintswe.foodmagnet.model;

/**
 * Created by poepoe on 7/3/15.
 */
public class ErrorResponse {
  public Error error;

  public static class Error {
    public Data data;

    public static class Data {
      public String message;
    }
  }
}
