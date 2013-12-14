package com.codeaffine.rose;

import static com.codeaffine.rose.RoseDrawer.IMAGE_SIDE_LENGTH;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SectionObserverTest {

  private static final int CENTER = IMAGE_SIDE_LENGTH / 2;

  private Label control;
  private Image rose;
  private SectionObserver sectionObserver;
  private SectionMarker sectionMarker;
  private DragMarker dragMarker;
  private DragDetector dragDetector;

  @Before
  public void setUp() {
    Display.getDefault();
    rose = new RoseDrawer().drawRose();
    control = mockLabel( rose );
    sectionMarker = mockSectionMarker();
    dragMarker = mockDragMarker();
    dragDetector = mock( DragDetector.class );
    sectionObserver = new SectionObserver( control, rose, sectionMarker, dragMarker, dragDetector );
  }

  @Test
  public void testSectionObserverRegistersListeners() {
    verify( control ).addMouseListener( sectionObserver );
    verify( control ).addMouseMoveListener( sectionObserver );
    verify( control ).addMouseTrackListener( sectionObserver );
    verify( control ).addDragDetectListener( sectionObserver );
  }

  @Test
  public void testDragDetected() {
    sectionObserver.repeatHandler = mock( RepeatHandler.class );

    sectionObserver.dragDetected( newDragEvent( CENTER, CENTER ) );

    assertTrue( sectionObserver.drag );
    assertNull( sectionObserver.repeatHandler );
    verify( dragDetector ).dragHandled();
  }

  @Test
  public void testDragIsIgnoredOutsideOfSphere() {
    sectionObserver.dragDetected( newDragEvent( 0, 0 ) );

    assertFalse( sectionObserver.drag );
  }

  @Test
  public void testDragDetectedClearsMarker() {
    Image marker = newImage();
    control.setImage( marker );

    sectionObserver.dragDetected( newDragEvent( CENTER, CENTER ) );

    assertSame( rose, control.getImage() );
    assertTrue( marker.isDisposed() );
  }

  @Test
  public void testMouseExit() {
    sectionObserver.drag = true;
    sectionObserver.mouseOver = true;

    sectionObserver.mouseExit( null );

    assertFalse( sectionObserver.drag );
    assertFalse( sectionObserver.mouseOver );
  }

  @Test
  public void testMouseExitClearsMarker() {
    Image marker = newImage();
    control.setImage( marker );

    sectionObserver.mouseExit( null );

    assertSame( rose, control.getImage() );
    assertTrue( marker.isDisposed() );
  }

  @Test
  public void testMouseEnter() {
    sectionObserver.mouseEnter( null );

    assertTrue( sectionObserver.mouseOver );
  }

  @Test
  public void testMouseUp() {
    sectionObserver.drag = true;
    sectionObserver.mouseOver = true;
    sectionObserver.repeatHandler = mock( RepeatHandler.class );

    sectionObserver.mouseUp( newMouseEvent( CENTER, CENTER ) );

    assertFalse( sectionObserver.drag );
    assertTrue( sectionObserver.mouseOver );
    assertNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testMouseUpSelectsMarker() {
    Image marker = newImage();
    control.setImage( marker );

    sectionObserver.mouseUp( newMouseEvent( CENTER, CENTER ) );

    assertNotSame( rose, control.getImage() );
    assertTrue( marker.isDisposed() );
  }

  @Test
  public void testMouseMoveWithoutDrag() {
    sectionObserver.mouseOver = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertNotSame( rose, control.getImage() );
  }

  @Test
  public void testMouseMoveWithoutDragClearsOldMarker() {
    Image oldMarker = newImage();
    control.setImage( oldMarker );
    sectionObserver.mouseOver = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertNotSame( rose, control.getImage() );
    assertTrue( oldMarker.isDisposed() );
  }

  @Test
  public void testMouseMoveWithoutDragIgnoresPositionsOutsideLabel() {
    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertSame( rose, control.getImage() );
  }

  @Test
  public void testMouseMoveWithoutDragIgnoresPositionsOutsideSphere() {
    sectionObserver.mouseOver = true;

    sectionObserver.mouseMove( newMouseEvent( 0, 0 ) );

    assertSame( rose, control.getImage() );
  }

  @Test
  public void testMouseMoveInvokesDragDetector() {
    MouseEvent mouseEvent = newMouseEvent( CENTER, CENTER );

    sectionObserver.mouseMove( mouseEvent );

    verify( dragDetector ).mouseMove( mouseEvent );
  }

  @Test
  public void testMouseMoveWithDrag() {
    sectionObserver.mouseOver = true;
    sectionObserver.drag = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertNotSame( rose, control.getImage() );
  }

  @Test
  public void testMouseMoveWithDragClearsOldMarker() {
    Image oldMarker = newImage();
    control.setImage( oldMarker );
    sectionObserver.mouseOver = true;
    sectionObserver.drag = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertNotSame( rose, control.getImage() );
    assertTrue( oldMarker.isDisposed() );
  }

  @Test
  public void testMouseMoveWithDragButWithoutMouseOverDoesNothing() {
    sectionObserver.drag = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    assertSame( rose, control.getImage() );
  }

  @Test
  public void testMouseMoveWithDragNotifiesSelection() {
    ArgumentCaptor<RoseNavigationEvent> captor = forClass( RoseNavigationEvent.class );
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );
    sectionObserver.mouseOver = true;
    sectionObserver.drag = true;

    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    verify( listener ).selected( captor.capture() );
    assertSame( Direction.S, captor.getValue().getDirection() );
    assertSame( Sphere.CORE, captor.getValue().getSphere() );
    assertTrue( captor.getValue().isDrag() );
    assertNotNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testMouseMoveWithDragNotifiesSelectionIfOutsideSphere() {
    ArgumentCaptor<RoseNavigationEvent> captor = forClass( RoseNavigationEvent.class );
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );
    sectionObserver.mouseOver = true;
    sectionObserver.drag = true;

    sectionObserver.mouseMove( newMouseEvent( 0, 0 ) );

    verify( listener ).selected( captor.capture() );
    assertSame( Direction.NW, captor.getValue().getDirection() );
    assertSame( Sphere.MANTLE, captor.getValue().getSphere() );
    assertTrue( captor.getValue().isDrag() );
    assertNotNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testMouseDownWithoutDragNotifiesSelection() {
    ArgumentCaptor<RoseNavigationEvent> captor = forClass( RoseNavigationEvent.class );
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );
    sectionObserver.mouseOver = true;

    sectionObserver.mouseDown( newMouseEvent( CENTER, CENTER ) );

    verify( listener ).selected( captor.capture() );
    assertSame( Direction.S, captor.getValue().getDirection() );
    assertSame( Sphere.CORE, captor.getValue().getSphere() );
    assertFalse( captor.getValue().isDrag() );
    assertNotNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testMouseDownOutsideMantelDoesNotNotifySelection() {
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );
    sectionObserver.mouseOver = true;

    sectionObserver.mouseDown( newMouseEvent( 0, 0 ) );

    verify( listener, never() ).selected( any( RoseNavigationEvent.class ) );
    assertNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testMouseDownWithoutMouseOverDoesNotNotifySelection() {
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );

    sectionObserver.mouseDown( newMouseEvent( CENTER, CENTER ) );

    verify( listener, never() ).selected( any( RoseNavigationEvent.class ) );
    assertNull( sectionObserver.repeatHandler );
  }

  @Test
  public void testRemoveRoseNavigationListener() {
    RoseNavigationListener listener = mock( RoseNavigationListener.class );
    sectionObserver.addRoseNavigationListener( listener );
    sectionObserver.mouseOver = true;
    sectionObserver.drag = true;

    sectionObserver.removeRoseNavigationListener( listener );
    sectionObserver.mouseMove( newMouseEvent( CENTER, CENTER ) );

    verify( listener, never() ).selected( any( RoseNavigationEvent.class ) );
  }

  private static Label mockLabel( Image image ) {
    final Label result = mock( Label.class );
    doAnswer( newImageApplicator( result ) ).when( result ).setImage( any( Image.class ) );
    result.setImage( image );
    Display display = mock( Display.class );
    when( result.getDisplay() ).thenReturn( display );
    return result;
  }

  private static SectionMarker mockSectionMarker() {
    SectionMarker result = mock( SectionMarker.class );
    when( result.markSection( any( Sphere.class ), any( Direction.class ) ) )
      .thenAnswer( newImageCreator() );
    return result;
  }

  private static DragMarker mockDragMarker() {
    DragMarker result = mock( DragMarker.class );
    when( result.markDragCoordinates( anyInt(),
                                      anyInt(),
                                      any( Direction.class ),
                                      any( Sphere.class ) ) )
      .thenAnswer( newImageCreator() );
    return result;
  }

  private static Answer<Image> newImageCreator() {
    return new Answer<Image>() {
      public Image answer( InvocationOnMock invocation ) throws Throwable {
        return newImage();
      }
    };
  }

  private static Answer<Object> newImageApplicator( final Label control ) {
    return new Answer<Object>() {
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        when( control.getImage() ).thenReturn( ( Image )invocation.getArguments()[ 0 ] );
        return null;
      }
    };
  }

  private DragDetectEvent newDragEvent( int x, int y ) {
    DragDetectEvent result = new DragDetectEvent( newEvent() );
    result.x = x;
    result.y = y;
    return result;
  }

  private MouseEvent newMouseEvent( int x, int y ) {
    MouseEvent result = new MouseEvent( newEvent() );
    result.x = x;
    result.y = y;
    return result;
  }

  private Event newEvent() {
    Event result = new Event();
    result.widget = control;
    return result;
  }

  private static Image newImage() {
    return new Image( null, new Rectangle( 0 , 0, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH ) );
  }
}