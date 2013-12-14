package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.DRAG_MARKER_SIDE_LENGTH;
import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static com.codeaffine.rose.RoseDrawer.RING_WIDTH;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class DragMarker {

  private final static int DRAG_MARKER_BOUND_OFF_SET
    = DRAG_MARKER_SIDE_LENGTH / 2 + RING_WIDTH / 2 + 2;

  private final ImageData roseData;

  DragMarker( ImageData roseData ) {
    this.roseData = ( ImageData )roseData.clone();
  }

  public Image markDragCoordinates( int x, int y, Direction direction, Sphere sphere ) {
    Point center = new Point( x, y );
    if( sphere != Sphere.CORE ) {
      center = direction.centerOnSector( x, y );
    }
    Point location = clipRangeIfNeeded( center );
    Image result = new Image( Display.getCurrent(), ( ImageData )roseData.clone() );
    new RoseDrawer().drawDragMarker( result, location.x, location.y );
    return result;
  }

  private static Point clipRangeIfNeeded( Point coordinates ) {
    int resultX = coordinates.x;
    int resultY = coordinates.y;
    if( 0 > resultX - DRAG_MARKER_BOUND_OFF_SET ) {
      resultX = DRAG_MARKER_BOUND_OFF_SET;
    }
    if( 0 > resultY - DRAG_MARKER_BOUND_OFF_SET ) {
      resultY = DRAG_MARKER_BOUND_OFF_SET;
    }
    if( IMAGE_SIDE_LENGTH < resultX + DRAG_MARKER_BOUND_OFF_SET ) {
      resultX = IMAGE_SIDE_LENGTH - DRAG_MARKER_BOUND_OFF_SET;
    }
    if( IMAGE_SIDE_LENGTH < resultY + DRAG_MARKER_BOUND_OFF_SET ) {
      resultY = IMAGE_SIDE_LENGTH - DRAG_MARKER_BOUND_OFF_SET;
    }
    return new Point( resultX, resultY );
  }
}