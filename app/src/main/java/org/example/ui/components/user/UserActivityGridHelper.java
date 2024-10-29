package org.example.ui.components.user;

import com.vaadin.flow.component.grid.Grid;
import org.example.domain.model.HasUserActivity;

import java.util.Comparator;
import java.util.function.Function;

public final class UserActivityGridHelper
{
    private UserActivityGridHelper()
    {
    }

    public static <T> Grid.Column<T> addUserActivityColumn(
            final Grid<T> grid,
            final Function<T, HasUserActivity.UserActivityState> stateExtractor,
            final boolean displayActive)
    {
        return grid.addColumn(v -> new UserActivityValueProvider(displayActive).apply(stateExtractor.apply(v)))
                .setHeader("Aktivität")
                .setFlexGrow(1)
                .setSortable(true)
                .setResizable(true)
                .setComparator(Comparator.comparing(stateExtractor));
    }
}

