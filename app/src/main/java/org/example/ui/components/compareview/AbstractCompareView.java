package org.example.ui.components.compareview;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractCompareView<M> extends Composite<VerticalLayout> implements AfterNavigationObserver
{
    protected final Grid<M> grAuswertung = new Grid<>();

    @Override
    public void afterNavigation(final AfterNavigationEvent event)
    {
    }

    public void showData(final Collection<M> items)
    {
        this.grAuswertung.setVisible(true);
        this.grAuswertung.setItems(items);
    }

    public void showLoading()
    {
        this.grAuswertung.setVisible(false);
        this.grAuswertung.setItems(new ArrayList<>());
    }

    public void showFailed(final String errorMsg)
    {
        this.grAuswertung.setVisible(false);
        this.grAuswertung.setItems(new ArrayList<>());
    }
}

