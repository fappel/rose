package com.codeaffine.rose;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

public class RoseNavigationTest {

  private Shell shell;

  @Test
  public void testConstructor() {
    RoseNavigation roseNavigation = new RoseNavigation( shell );
    roseNavigation.addRoseNavigationListener( new RoseNavigationListener() {
      public void selected( RoseNavigationEvent event ) {

        System.out.println( event.getDirection() + "-" + event.getSphere() );
      }
    } );

    // TODO [fappel]: writeTests
    showShell( shell );
  }

  @Before
  public void setUp() {
    shell = new Shell( SWT.SHELL_TRIM );
    shell.setSize( 200, 200 );
    shell.setLayout( new FillLayout() );
    shell.open();
  }

  public static void showShell( Shell shell ) {
    shell.layout();
    while( !shell.isDisposed() ) {
      if( !shell.getDisplay().readAndDispatch() ) {
        shell.getDisplay().sleep();
      }
    }
  }
}