package com.codeaffine.rose;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RoseNavigationEventTest {

  @Test
  public void testGetDirection() {
    RoseNavigationEvent event = new RoseNavigationEvent( Direction.N, Sphere.CORE, false );

    Direction actual = event.getDirection();

    assertSame( Direction.N, actual );
  }

  @Test
  public void testGetSphere() {
    RoseNavigationEvent event = new RoseNavigationEvent( Direction.N, Sphere.CORE, false );

    Sphere actual = event.getSphere();

    assertSame( Sphere.CORE, actual );
  }

  @Test
  public void testIsDrag() {
    RoseNavigationEvent event = new RoseNavigationEvent( Direction.N, Sphere.CORE, true );

    boolean actual = event.isDrag();

    assertTrue( actual );
  }
}