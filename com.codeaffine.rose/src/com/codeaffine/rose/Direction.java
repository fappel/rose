package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static com.codeaffine.rose.SectionMath.calculateRadius;
import static com.codeaffine.rose.SectionMath.calculateX;
import static com.codeaffine.rose.SectionMath.calculateY;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;

import org.eclipse.swt.graphics.Point;

public enum Direction {

  N( new NSector() ),
  NE( new NESector() ),
  E( new ESector() ),
  SE( new SESector() ),
  S( new SSector() ),
  SW( new SWSector() ),
  W( new WSector() ),
  NW( new NWSector() );

  private static class NSector implements Sector, Shape {

    private static final Point ANGLES = new Point( 45, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      return new Point( IMAGE_SIDE_LENGTH / 2, y );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return abs( angle ) > PI * 7 / 8;
    }
  }

  private static class NESector implements Sector, Shape {

    private static final Point ANGLES = new Point( 360, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      double radius = calculateRadius( x, y );
      int newX = calculateX( radius, -( PI * 1 / 4 ) );
      int newY = calculateY( radius, -( PI * 1 / 4 ) );
      return new Point( newX, newY );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle < PI * 7 / 8 && angle > PI * 5 / 8;
    }
  }

  private static class NWSector implements Sector, Shape {

    private static final Point ANGLES = new Point( 90, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      double radius = calculateRadius( x, y );
      int newX = calculateX( radius, -( PI * 3 / 4 ) );
      int newY = calculateY( radius, -( PI * 3 / 4 ) );
      return new Point( newX, newY );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle > -( PI * 7 / 8 ) && angle < -( PI * 5 / 8 );
    }
  }

  private static class SSector implements Sector, Shape {

    private static final Point ANGLES = new Point( 225, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      return new Point( IMAGE_SIDE_LENGTH / 2, y );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return abs( angle ) < PI * 1 / 8;
    }
  }

  private static class SESector implements Sector, Shape {

    private static final Point ANGLES = new Point( 270, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      double radius = calculateRadius( x, y );
      int newX = calculateX( radius, PI * 1 / 4 );
      int newY = calculateY( radius, PI * 1 / 4 );
      return new Point( newX, newY );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle < PI * 3 / 8 && angle > PI * 1 / 8;
    }
  }

  private static class SWSector implements Sector, Shape {

    private static final Point ANGLES = new Point( 180, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      double radius = calculateRadius( x, y );
      int newX = calculateX( radius, PI * 3 / 4 );
      int newY = calculateY( radius, PI * 3 / 4 );
      return new Point( newX, newY );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle > -( PI * 3 / 8 ) && angle < -( PI * 1 / 8 );
    }
  }

  private static class WSector implements Sector, Shape {

    private static final Point ANGLES = new Point( 135, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      return new Point( x, IMAGE_SIDE_LENGTH / 2 );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle < -( PI * 3 / 8 ) && angle > -( PI * 5 / 8 );
    }
  }

  private static class ESector implements Sector, Shape {

    private static final Point ANGLES = new Point( 315, 90 );

    public Point getAngles() {
      return ANGLES;
    }

    public Point center( int x, int y ) {
      return new Point( x, IMAGE_SIDE_LENGTH / 2 );
    }

    public boolean covers( int x, int y ) {
      float angle = calculateAngle( x, y );
      return angle > PI * 3 / 8 && angle < PI * 5 / 8;
    }
  }

  private final Sector sector;

  Direction( Sector sector ) {
    this.sector = sector;
  }

  boolean covers( int x, int y ) {
    return ( ( Shape )sector ).covers( x, y );
  }

  Point getSectorAngles() {
    return sector.getAngles();
  }

  Point centerOnSector( int x, int y ) {
    return sector.center( x, y );
  }

  static Direction valueOf( int x, int y ) {
    Direction result = null;
    for( Direction direction : values() ) {
      if( direction.covers( x, y ) ) {
        result = direction;
      }
    }
    return result;
  }

  static float calculateAngle( int x, int y ) {
    int dx = x - IMAGE_SIDE_LENGTH / 2;
    int dy = y - IMAGE_SIDE_LENGTH / 2;
    return ( float )atan2( dx, dy );
  }
}