package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static com.codeaffine.rose.SectionMath.calculateRadius;

public enum Sphere {

  CORE( new CoreSphere() ),
  MIDDLE( new MiddleRing() ),
  MANTLE( new MantleRing() );

  static final int CORE_UPPER_BOUND = IMAGE_SIDE_LENGTH / 4 - 2;
  static final int MIDDLE_LOWER_BOUND = CORE_UPPER_BOUND;
  static final int MIDDLE_UPPER_BOUND = IMAGE_SIDE_LENGTH * 3 / 8 - 1;
  static final int MANTLE_UPPER_BOUND = IMAGE_SIDE_LENGTH / 2 - 1;
  static final int MANTLE_LOWER_BOUND = IMAGE_SIDE_LENGTH * 3 / 8 - 1;

  private static class CoreSphere implements Shape {
    public boolean covers( int x, int y ) {
      double radius = calculateRadius( x, y );
      return radius < CORE_UPPER_BOUND;
    }
  }

  private static class MiddleRing implements Shape {
    public boolean covers( int x, int y ) {
      double radius = calculateRadius( x, y );
      return radius > MIDDLE_LOWER_BOUND && radius < MIDDLE_UPPER_BOUND;
    }
  }

  private static class MantleRing implements Shape {
    public boolean covers( int x, int y ) {
      double radius = calculateRadius( x, y );
      return radius > MANTLE_LOWER_BOUND && radius < MANTLE_UPPER_BOUND;
    }
  }

  private final Shape shape;

  Sphere( Shape shape ) {
    this.shape = shape;
  }

  public boolean covers( int x, int y ) {
    return shape.covers( x, y );
  }

  static Sphere valueOf( int x, int y ) {
    Sphere result = null;
    for( Sphere sphere : values() ) {
      if( sphere.covers( x, y ) ) {
        result = sphere;
      }
    }
    return result;
  }
}