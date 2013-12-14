package com.codeaffine.rose;

public class RoseNavigationEvent {

  private final Direction direction;
  private final Sphere sphere;
  private final boolean drag;

  RoseNavigationEvent( Direction direction, Sphere sphere, boolean drag ) {
    this.direction = direction;
    this.sphere = sphere;
    this.drag = drag;
  }

  public Direction getDirection() {
    return direction;
  }

  public Sphere getSphere() {
    return sphere;
  }

  public boolean isDrag() {
    return drag;
  }
}