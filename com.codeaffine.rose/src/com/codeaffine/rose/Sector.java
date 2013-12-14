package com.codeaffine.rose;

import org.eclipse.swt.graphics.Point;

public interface Sector {
  Point getAngles();
  Point center( int x, int y );
}
