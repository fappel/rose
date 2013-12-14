package com.codeaffine.rose;

import org.eclipse.swt.widgets.Display;

public class RepeatHandler implements Runnable {

  static final int DELAY = 50;

  private final SectionObserver sectionObserver;
  private final Direction direction;
  private final Sphere sphere;
  private final boolean drag;

  RepeatHandler( SectionObserver observer,
                 Display display,
                 Direction direction,
                 Sphere sphere,
                 boolean drag,
                 int delay )
  {
    this.sectionObserver = observer;
    this.direction = direction;
    this.sphere = sphere;
    this.drag = drag;
    display.timerExec( delay, this );
  }

  public void run() {
    if( sectionObserver.repeatHandler == this ) {
      sectionObserver.notifySelected( direction, sphere, drag, DELAY );
    }
  }
}