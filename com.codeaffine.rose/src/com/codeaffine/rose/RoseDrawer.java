package com.codeaffine.rose;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class RoseDrawer {

  static final int IMAGE_SIDE_LENGTH = 40;
  static final int DRAG_MARKER_SIDE_LENGTH = IMAGE_SIDE_LENGTH / 4;
  static final int RING_WIDTH = 5;

  private static final Color LIGHT_GRAY
    = Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
  private static final Color MEDIUM_GRAY
    = Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW );
  private static final Color DARK_GRAY
    = Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_FOREGROUND );
  private static final Color SELECTION
    = Display.getCurrent().getSystemColor( SWT.COLOR_LIST_SELECTION );

  private GC gc;

  void drawSection( Image img, int margin, int startAngle, int arcAngle, int lineWidth ) {
    gc = new GC( img );
    try {
      int sideLength = IMAGE_SIDE_LENGTH - margin * 2;
      drawArc( margin, margin, sideLength, sideLength, startAngle, arcAngle, lineWidth, SELECTION );
    } finally {
      gc.dispose();
    }
  }

  void drawDragMarker( Image image, int x, int y ) {
    gc = new GC( image );
    try {
      int sideLength = DRAG_MARKER_SIDE_LENGTH;
      int left = x - sideLength / 2;
      int top = y - sideLength / 2;
      drawArc( left, top, sideLength, sideLength, 0, 360, RING_WIDTH, SELECTION );
    } finally {
      gc.dispose();
    }
  }

  Image drawRose() {
    Image result = new Image( Display.getCurrent(), IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH );
    gc = new GC( result );
    try {
      drawCanvas();
      drawLightGrayRing( 3 );
      drawLightGrayRing( 15 );
      drawDarkGrayOrientationLines( 0 );
      drawDarkGrayArrowLines();
      drawDarkGrayOrientationLines( 10 );
      drawMediumGrayOrientationLines( 0 );
      drawMediumGrayOrientationLines( 7 );
    } finally {
      gc.dispose();
    }
    return result;
  }

  private void drawCanvas() {
    gc.setBackground( getColor( SWT.COLOR_LIST_BACKGROUND ) );
    gc.fillRectangle( 0, 0, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH );
  }

  private void drawLightGrayRing( int margin ) {
    int sideLength = IMAGE_SIDE_LENGTH - margin * 2;
    drawArc( margin, margin, sideLength, sideLength, 0, 360, RING_WIDTH, LIGHT_GRAY );
  }

  private void drawDarkGrayOrientationLines( int margin ) {
    int center = IMAGE_SIDE_LENGTH / 2;
    int smallOut = 1 + margin;
    int smallIn = RING_WIDTH + margin;
    int largeOut = IMAGE_SIDE_LENGTH - RING_WIDTH - margin;
    int largeIn = IMAGE_SIDE_LENGTH - margin - 1;
    drawOrientationLine( center, smallOut, center, smallIn, 1, DARK_GRAY );
    drawOrientationLine( largeOut, center, largeIn, center, 1, DARK_GRAY );
    drawOrientationLine( center, largeOut, center, largeIn, 1, DARK_GRAY );
    drawOrientationLine( smallOut, center, smallIn, center, 1, DARK_GRAY );
  }

  private void drawDarkGrayArrowLines() {
    int start = IMAGE_SIDE_LENGTH / 2 - 1;
    int end = IMAGE_SIDE_LENGTH / 2 + 1;
    int sideMargin = 2;
    int couterSideMargin = IMAGE_SIDE_LENGTH - sideMargin;
    drawOrientationLine( start, sideMargin, end, sideMargin, 1, DARK_GRAY );
    drawOrientationLine( couterSideMargin, start, couterSideMargin, end, 1, DARK_GRAY );
    drawOrientationLine( start, couterSideMargin, end, couterSideMargin, 1, DARK_GRAY );
    drawOrientationLine( sideMargin, start, sideMargin, end, 1, DARK_GRAY );
  }

  private void drawMediumGrayOrientationLines( int margin ) {
    int smallOffset = DRAG_MARKER_SIDE_LENGTH - RING_WIDTH / 2;
    int largeOffset = DRAG_MARKER_SIDE_LENGTH + RING_WIDTH / 2;
    int smallOut = smallOffset + margin;
    int smallIn = largeOffset - 2 + margin;
    int largeOut = IMAGE_SIDE_LENGTH / 2 + largeOffset + 1 - margin;
    int larginIn = IMAGE_SIDE_LENGTH / 2 + smallOffset + 3 - margin;
    drawOrientationLine( smallOut, smallOut, smallIn, smallIn, 1, MEDIUM_GRAY );
    drawOrientationLine( largeOut, smallOut, larginIn, smallIn, 1, MEDIUM_GRAY );
    drawOrientationLine( largeOut, largeOut, larginIn, larginIn, 1, MEDIUM_GRAY );
    drawOrientationLine( smallOut, largeOut, smallIn, larginIn, 1, MEDIUM_GRAY );
  }

  private void drawArc(
    int x, int y, int width, int height, int startAngle, int arcAngle, int lineWidth, Color color )
  {
    gc.setAntialias( SWT.ON );
    gc.setForeground( color );
    gc.setLineWidth( lineWidth );
    gc.drawArc( x, y, width, height, startAngle, arcAngle );
  }

  private void drawOrientationLine( int x1, int y1, int x2, int y2, int lineWidth, Color color ) {
    gc.setAntialias( SWT.ON );
    gc.setForeground( color );
    gc.setLineWidth( lineWidth );
    gc.drawLine( x1, y1, x2, y2 );
  }

  private static Color getColor( int color ) {
    return Display.getCurrent().getSystemColor( color );
  }
}