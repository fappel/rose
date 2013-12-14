package com.codeaffine.rose;

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SectionMathTest {

  private static final int CENTER = RoseDrawer.IMAGE_SIDE_LENGTH / 2;

  @Test
  public void testCalculateRadius() {
    double center = SectionMath.calculateRadius( CENTER, CENTER );
    double radius1 = SectionMath.calculateRadius( CENTER + 5, CENTER );
    double radius2 = SectionMath.calculateRadius( CENTER, CENTER + 5 );

    assertEquals( 0.0, center, 0 );
    assertEquals( 5.0, radius1, 0 );
    assertEquals( 5.0, radius2, 0 );
  }

  @Test
  public void testCalculateX() {
    int x_0_pi = SectionMath.calculateX( 0 , PI );
    int x_1_pi = SectionMath.calculateX( 1 , PI );
    int x_minus1_pi = SectionMath.calculateX( -1 , PI );
    int x_0_piHalf = SectionMath.calculateX( 0 , PI / 2 );
    int x_1_piHalf = SectionMath.calculateX( 1 , PI / 2 );
    int x_minus1_piHalf = SectionMath.calculateX( -1 , PI / 2 );

    assertEquals( CENTER, x_0_pi );
    assertEquals( CENTER - 1, x_1_pi );
    assertEquals( CENTER + 1, x_minus1_pi );
    assertEquals( CENTER, x_0_piHalf );
    assertEquals( CENTER, x_1_piHalf );
    assertEquals( CENTER, x_minus1_piHalf );
  }

  @Test
  public void testCalculateY() {
    int y_0_pi = SectionMath.calculateY( 0 , PI );
    int y_1_pi = SectionMath.calculateY( 1 , PI );
    int y_minus1_pi = SectionMath.calculateY( -1 , PI );
    int y_0_piHalf = SectionMath.calculateY( 0 , PI / 2 );
    int y_1_piHalf = SectionMath.calculateY( 1 , PI / 2 );
    int y_minus1_piHalf = SectionMath.calculateY( -1 , PI / 2 );

    assertEquals( CENTER, y_0_pi );
    assertEquals( CENTER, y_1_pi );
    assertEquals( CENTER, y_minus1_pi );
    assertEquals( CENTER, y_0_piHalf );
    assertEquals( CENTER + 1, y_1_piHalf );
    assertEquals( CENTER - 1, y_minus1_piHalf );
  }
}