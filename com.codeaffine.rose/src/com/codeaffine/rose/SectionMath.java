package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;

class SectionMath {

  private static final int HALF_IMAGE_SIDE_LENGTH = IMAGE_SIDE_LENGTH / 2;

  static double calculateRadius( int x, int y ) {
    return hypot( x - HALF_IMAGE_SIDE_LENGTH, y - HALF_IMAGE_SIDE_LENGTH );
  }

  static int calculateX( double radius, double angle ) {
    return ( int )( radius * cos( angle ) ) + HALF_IMAGE_SIDE_LENGTH;
  }

  static int calculateY( double radius, double angle ) {
    return ( int )( radius * sin( angle ) ) + HALF_IMAGE_SIDE_LENGTH;
  }
}