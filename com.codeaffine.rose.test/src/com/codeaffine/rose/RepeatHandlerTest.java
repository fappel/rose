package com.codeaffine.rose;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

public class RepeatHandlerTest {

  private SectionObserver observer;
  private Display display;
  private RepeatHandler handler;

  @Test
  public void testConstructorSchedulesRepeat() {
    verify( display ).timerExec( 10, handler );
  }

  @Test
  public void testRunRepeatsNotification() {
    observer.repeatHandler = handler;

    handler.run();

    verify( observer ).notifySelected( Direction.N, Sphere.CORE, false, RepeatHandler.DELAY );
  }

  @Test
  public void testRunDoesNotRepeatNotificationIfHandlerWasCleared() {
    handler.run();

    verify( observer, never() )
      .notifySelected( Direction.N, Sphere.CORE, false, RepeatHandler.DELAY );
  }

  @Before
  public void setUp() {
    observer = mock( SectionObserver.class );
    display = mock( Display.class );
    handler = new RepeatHandler( observer, display, Direction.N, Sphere.CORE, false, 10 );
  }
}