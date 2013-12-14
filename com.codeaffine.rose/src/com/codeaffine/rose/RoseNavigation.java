package com.codeaffine.rose;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class RoseNavigation {

  private static final Color BACK_GROUND
    = Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND );

  private final SectionObserver sectionObserver;
  private final Composite control;

  public RoseNavigation( Composite parent ) {

    control = new Composite( parent, SWT.NONE );
    control.setBackground( BACK_GROUND );
    control.setLayout( new FormLayout() );

    Label imageHolder = new Label( control, SWT.NONE );
    imageHolder.setBackground( BACK_GROUND );
    Image rose = new RoseDrawer().drawRose();
    imageHolder.setImage( rose );
    imageHolder.pack();

    FormData imageHolderData = new FormData();
    imageHolderData.left = new FormAttachment( 50, - RoseDrawer.IMAGE_SIDE_LENGTH / 2 );
    imageHolderData.top = new FormAttachment( 50, - RoseDrawer.IMAGE_SIDE_LENGTH / 2 );
    imageHolder.setLayoutData( imageHolderData );
    parent.layout();

    sectionObserver = new SectionObserver( imageHolder );
  }

  public Control getControl() {
    return control;
  }

  public void addRoseNavigationListener( RoseNavigationListener roseNavigationListener ) {
    sectionObserver.addRoseNavigationListener( roseNavigationListener );
  }

  public void removeRoseNavigationListener( RoseNavigationListener roseNavigationListener ) {
    sectionObserver.removeRoseNavigationListener( roseNavigationListener );
  }
}