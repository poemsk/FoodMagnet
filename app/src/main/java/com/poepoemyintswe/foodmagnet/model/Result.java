package com.poepoemyintswe.foodmagnet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by poepoe on 2/4/15.
 */
public class Result implements Serializable {
  public Geometry geometry;
  public String icon;
  public String id;
  public String name;
  public OpeningHours opening_hours;
  public List<Photo> photos;
  public String place_id;
  public String scope;
  public String reference;
  public List<String> types;
  public String vicinity;
}
