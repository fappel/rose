package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.RING_WIDTH;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class SectionMarker {

  private final ImageData roseData;

  SectionMarker( ImageData roseData ) {
    this.roseData = ( ImageData )roseData.clone();
  }

  public Image markSection( Sphere sphere, Direction direction ) {
    Image result = null;
    switch( sphere ) {
      case CORE:
        result = drawCoreSelection();
        break;
      case MIDDLE:
        result = drawMiddleSelection( direction );
        break;
      case MANTLE:
        result = drawMantelSelection( direction );
        break;
    }
    return result;
  }

  private Image drawCoreSelection() {
    return drawSection( 16, 0, 360, RING_WIDTH );
  }

  private Image drawMiddleSelection( Direction direction ) {
    Point angles = direction.getSectorAngles();
    return drawSection( 9, angles.x, angles.y, RING_WIDTH + 2 );
  }

  private Image drawMantelSelection( Direction direction ) {
    Point angles = direction.getSectorAngles();
    return drawSection( 4, angles.x, angles.y, RING_WIDTH );
  }

  private Image drawSection( int margin, int startAngle, int arcAngle, int ringWidth ) {
    Image result = new Image( Display.getCurrent(), ( ImageData )roseData.clone() );
    new RoseDrawer().drawSection( result, margin, startAngle, arcAngle, ringWidth );
    return result;
  }
}