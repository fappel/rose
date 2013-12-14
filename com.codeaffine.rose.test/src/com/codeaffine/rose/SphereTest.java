package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static com.google.common.collect.Lists.newLinkedList;
import static java.lang.Math.PI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith( value = Parameterized.class )
public class SphereTest {

  private static final int[][] BOUNDS = new int[][] {
    { 0, Sphere.CORE_UPPER_BOUND },
    { Sphere.MIDDLE_LOWER_BOUND, Sphere.MIDDLE_UPPER_BOUND },
    { Sphere.MANTLE_LOWER_BOUND, Sphere.MANTLE_UPPER_BOUND }
  };

  private final Sphere match;
  private final Sphere noMatch1;
  private final Sphere noMatch2;
  private final int x;
  private final int y;
  private final int upperBound;
  private final int lowerBound;

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = newLinkedList();
    Sphere[] sphere = Sphere.values();
    for( int i = 0; i < sphere.length; i++ ) {
      Sphere match = sphere[ i ];
      Sphere noMatch1 = sphere[ i == 0 ? 2 : i - 1 ];
      Sphere noMatch2 = sphere[ i == 2 ? 0 : i + 1 ];
      int[] bounds = BOUNDS[ i ];
      int coordinate = IMAGE_SIDE_LENGTH / 2 - ( ( i + 1 ) * IMAGE_SIDE_LENGTH / 6 ) + 7;
      result.add( new Object[] {
        match, noMatch1, noMatch2, coordinate, coordinate, bounds[ 0 ], bounds[ 1 ]
      } );
    }
    return result;
  }

  public SphereTest(
    Sphere match, Sphere noMatch1, Sphere noMatch2, int x, int y, int lowerBound, int upperBound )
  {
    this.match = match;
    this.noMatch1 = noMatch1;
    this.noMatch2 = noMatch2;
    this.x = x;
    this.y = y;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  @Test
  public void testCovers() {
    assertTrue( match.covers( calculateX( lowerBound ) + 1, calculateY( lowerBound ) + 1 ) );
    assertTrue( match.covers( x, y ) );
    assertTrue( match.covers( calculateX( upperBound ), calculateY( upperBound ) ) );
    assertFalse( match.covers( 0, 0 ) );
    assertFalse( noMatch1.covers( x, y ) );
    assertFalse( noMatch1.covers( calculateX( lowerBound ) + 1, calculateY( lowerBound ) + 1 ) );
    assertFalse( noMatch1.covers( x, y ) );
    assertFalse( noMatch2.covers( x, y ) );
    assertFalse( noMatch2.covers( calculateX( lowerBound ) + 1, calculateY( lowerBound ) + 1 ) );
    assertFalse( noMatch2.covers( x, y ) );
  }

  @Test
  public void testValueOf() {
    assertSame( match, Sphere.valueOf( x, y ) );
    assertNull( Sphere.valueOf( 0, 0 ) );
  }

  private static int calculateX( double radius ) {
    return SectionMath.calculateX( radius, PI / 4 );
  }

  private static int calculateY( double radius ) {
    return SectionMath.calculateY( radius, PI / 4 );
  }
}