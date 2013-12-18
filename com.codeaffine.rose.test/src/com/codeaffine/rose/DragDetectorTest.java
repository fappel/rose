package com.codeaffine.rose;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DragDetectorTest {

  private Control control;
  private DragDetector dragDetector;

  @Test
  public void testDragDetectorDisablesDragDetection() {
    verify( control ).setDragDetect( false );
  }

  @Test
  public void testMouseMoveTriggersDrag() {
    MouseEvent mouseEvent = newMouseEvent( control );
    mouseEvent.stateMask = SWT.BUTTON1;
    mouseEvent.x = 10;
    mouseEvent.y = 10;

    dragDetector.mouseMove( mouseEvent );

    ArgumentCaptor<Event> captor = ArgumentCaptor.forClass( Event.class );
    verify( control ).notifyListeners( eq( SWT.DragDetect ), captor.capture() );
    assertEquals( mouseEvent.x, captor.getValue().x );
    assertEquals( mouseEvent.y, captor.getValue().y );
    assertEquals( SWT.DragDetect, captor.getValue().type );
    assertTrue( dragDetector.dragEventGenerated );
    assertEquals( mouseEvent.x, dragDetector.lastMouseX );
    assertEquals( mouseEvent.y, dragDetector.lastMouseY );
  }

  @Test
  public void testMouseMoveDoesNotTriggerDragIfMovementIsTooSmall() {
    MouseEvent mouseEvent = newMouseEvent( control );
    mouseEvent.stateMask = SWT.BUTTON1;
    mouseEvent.x = 1;
    mouseEvent.y = 1;

    dragDetector.mouseMove( mouseEvent );

    verify( control, never() ).notifyListeners( eq( SWT.DragDetect ), any( Event.class ) );
    assertEquals( mouseEvent.x, dragDetector.lastMouseX );
    assertEquals( mouseEvent.y, dragDetector.lastMouseY );
  }

  @Test
  public void testMouseMoveIgnoresDragIfButton1StateMaskIsNotSet() {
    MouseEvent mouseEvent = newMouseEvent( control );
    mouseEvent.stateMask = SWT.BUTTON2;
    mouseEvent.x = 10;
    mouseEvent.y = 10;

    dragDetector.mouseMove( mouseEvent );

    verify( control, never() ).notifyListeners( eq( SWT.DragDetect ), any( Event.class ) );
    assertEquals( 0, dragDetector.lastMouseX );
    assertEquals( 0, dragDetector.lastMouseY );
  }

  @Test
  public void testMouseMoveDoesNotTriggerDragIfLastDragEventIsNotHandledYet() {
    MouseEvent mouseEvent = newMouseEvent( control );
    mouseEvent.stateMask = SWT.BUTTON1;
    mouseEvent.x = 10;
    mouseEvent.y = 10;
    dragDetector.dragEventGenerated = true;

    dragDetector.mouseMove( mouseEvent );

    verify( control, never() ).notifyListeners( eq( SWT.DragDetect ), any( Event.class ) );
    assertEquals( mouseEvent.x, dragDetector.lastMouseX );
    assertEquals( mouseEvent.y, dragDetector.lastMouseY );
  }

  @Test
  public void testDragHandled() {
    dragDetector.dragEventGenerated = true;

    dragDetector.dragHandled();

    assertFalse( dragDetector.dragEventGenerated );
  }

  @Before
  public void setUp() {
    control = mock( Control.class );
    dragDetector = new DragDetector( control, 3 );
  }

  private static MouseEvent newMouseEvent( Control control ) {
    Event event = newEvent( control );
    return new MouseEvent( event );
  }

  private static Event newEvent( Control control ) {
    Event result = new Event();
    result.widget = control;
    return result;
  }
}