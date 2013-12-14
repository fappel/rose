package com.codeaffine.rose;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

public class SectionObserver
  implements MouseListener, MouseMoveListener, MouseTrackListener, DragDetectListener
{

  private static final int REPEAT_DELAY = 300;

  boolean mouseOver;
  boolean drag;
  RepeatHandler repeatHandler;

  private final SectionMarker sectionMarker;
  private final DragMarker dragMarker;
  private final DragDetector dragDetector;
  private final Label control;
  private final Image rose;
  private final Set<RoseNavigationListener> listeners;

  SectionObserver( Label control ) {
    this( control,
          getRoseImage( control ),
          newSectionMarker( control ),
          newDragMarker( control ),
          new DragDetector( control, 3 ) );
  }

  SectionObserver( Label control,
                   Image rose,
                   SectionMarker sectionMarker,
                   DragMarker dragMarker,
                   DragDetector dragDetector )
  {
    this.control = control;
    this.rose = rose;
    this.sectionMarker = sectionMarker;
    this.dragMarker = dragMarker;
    this.listeners = newHashSet();
    this.dragDetector = dragDetector;
    control.addMouseListener( this );
    control.addMouseMoveListener( this );
    control.addMouseTrackListener( this );
    control.addDragDetectListener( this );
  }

  public void addRoseNavigationListener( RoseNavigationListener roseNavigationListener ) {
    listeners.add( roseNavigationListener );
  }

  public void removeRoseNavigationListener( RoseNavigationListener roseNavigationListener ) {
    listeners.remove( roseNavigationListener );
  }

  public void dragDetected( DragDetectEvent e ) {
    repeatHandler = null;
    if( Sphere.CORE == Sphere.valueOf( e.x, e.y ) ) {
      drag = true;
      clearMarker();
    }
    dragDetector.dragHandled();
  }

  public void mouseMove( MouseEvent e ) {
    dragDetector.mouseMove( e );
    if( mouseOver ) {
      handleMouseMove( e );
    }
  }

  public void mouseEnter( MouseEvent e ) {
    mouseOver = true;
  }

  public void mouseExit( MouseEvent e ) {
    mouseOver = false;
    drag = false;
    clearMarker();
  }

  public void mouseHover( MouseEvent e ) {
  }

  public void mouseDoubleClick( MouseEvent e ) {
  }

  public void mouseDown( MouseEvent e ) {
    if( mouseOver ) {
      notifyAboutSelection( e );
    }
  }

  public void mouseUp( MouseEvent e ) {
    drag = false;
    repeatHandler = null;
    handleMouseMove( e );
  }

  private void handleMouseMove( MouseEvent e ) {
    if( drag ) {
      markDrag( e );
      notifyAboutDrag( e );
    } else {
      markSection( e );
    }
  }

  private void markDrag( MouseEvent e ) {
    clearMarker();
    Sphere sphere = Sphere.valueOf( e.x, e.y );
    Direction direction = Direction.valueOf( e.x, e.y );
    control.setImage( dragMarker.markDragCoordinates( e.x, e.y, direction, sphere ) );
  }

  private void markSection( MouseEvent e ) {
    Sphere sphere = Sphere.valueOf( e.x, e.y );
    clearMarker();
    if( sphere != null ) {
      Direction direction = Direction.valueOf( e.x, e.y );
      control.setImage( sectionMarker.markSection( sphere, direction ) );
    }
  }

  private void clearMarker() {
    if( control.getImage() != rose ) {
      control.getImage().dispose();
      control.setImage( rose );
    }
  }

  private void notifyAboutSelection( MouseEvent e ) {
    Sphere sphere = Sphere.valueOf( e.x, e.y );
    if( sphere != null ) {
      Direction direction = Direction.valueOf( e.x, e.y );
      notifySelected( direction, sphere, false, REPEAT_DELAY );
    }
  }

  private void notifyAboutDrag( MouseEvent e ) {
    int delay = repeatHandler == null ? REPEAT_DELAY : RepeatHandler.DELAY; // TODO [fappel]: test
    repeatHandler = null;
    Sphere sphere = Sphere.valueOf( e.x, e.y ) == null ? Sphere.MANTLE : Sphere.valueOf( e.x, e.y );
    Direction direction = Direction.valueOf( e.x, e.y );
    notifySelected( direction, sphere, true, delay );
  }

  protected void notifySelected( Direction direction, Sphere sphere, boolean drag, int delay ) {
    Iterator<RoseNavigationListener> iterator = listeners.iterator();
    while( iterator.hasNext() ) {
      iterator.next().selected( new RoseNavigationEvent( direction, sphere, drag ) );
    }
    repeatHandler = new RepeatHandler( this, control.getDisplay(), direction, sphere, drag, delay );
  }

  private static DragMarker newDragMarker( Label control ) {
    return new DragMarker( getRoseImage( control ).getImageData() );
  }

  private static SectionMarker newSectionMarker( Label control ) {
    return new SectionMarker( getRoseImage( control ).getImageData() );
  }

  private static Image getRoseImage( Label control ) {
    return control.getImage();
  }
}