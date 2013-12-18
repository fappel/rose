package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static com.google.common.collect.Lists.newLinkedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith( value = Parameterized.class )
public class DirectionTest {

  private final static int[][] TEST_COORDINATES = new int[][] {
    { IMAGE_SIDE_LENGTH / 2, 0 },
    { IMAGE_SIDE_LENGTH, 0 },
    { IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH / 2 },
    { IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH },
    { IMAGE_SIDE_LENGTH / 2, IMAGE_SIDE_LENGTH },
    { 0, IMAGE_SIDE_LENGTH },
    { 0, IMAGE_SIDE_LENGTH / 2 },
    { 0, 0 }
  };

  private final static Point[] EXPECTED_CENTERED = new Point[] {
    new Point( 20, 0 ),
    new Point( 40, 0 ),
    new Point( 40, 20 ),
    new Point( 40, 40 ),
    new Point( 20, 40 ),
    new Point( 0, 40 ),
    new Point( 0, 20 ),
    new Point( 0, 0 )
  };

  private final Direction match;
  private final Direction before;
  private final Direction after;
  private final Point expectedCentered;
  private final int x;
  private final int y;

  public DirectionTest(
    Direction match, Direction before, Direction after, Point centered, int x, int y )
  {
    this.match = match;
    this.before = before;
    this.after = after;
    this.expectedCentered = centered;
    this.x = x;
    this.y = y;
  }

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = newLinkedList();
    Direction[] directions = Direction.values();
    for( int i = 0; i < directions.length; i++ ) {
      Direction match = directions[ i ];
      Direction before = directions[ i == 0 ? 7 : i - 1 ];
      Direction after = directions[ i == 7 ? 0 : i + 1 ];
      int[] point = TEST_COORDINATES[ i ];
      Point centered = EXPECTED_CENTERED[ i ];
      result.add( new Object[] { match, before, after, centered, point[ 0 ], point[ 1 ] } );
    }
    return result;
  }

  @Test
  public void testCovers() {
    assertTrue( match.covers( x, y ) );
    assertFalse( before.covers( x, y ) );
    assertFalse( after.covers( x, y ) );
  }

  @Test
  public void testGetSectorAngles() {
    assertNotNull( match.getSectorAngles() );
  }

  @Test
  public void testCenter() {
    Point actual = match.centerOnSector( x, y );

    assertEquals( expectedCentered, actual );
  }

  @Test
  public void testValueOf() {
    assertSame( match, Direction.valueOf( x, y ) );
  }
}