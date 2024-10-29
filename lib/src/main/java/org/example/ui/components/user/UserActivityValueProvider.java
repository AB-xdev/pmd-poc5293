package org.example.ui.components.user;

import org.example.domain.model.HasUserActivity;

import java.util.Optional;
import java.util.function.Function;

public class UserActivityValueProvider implements Function<HasUserActivity.UserActivityState, String>
{
    private final boolean displayActive;

    public UserActivityValueProvider(final boolean displayActive)
    {
        this.displayActive = displayActive;
    }

    @Override
    public String apply(final HasUserActivity.UserActivityState userActivityState)
    {
        return this.applyOpt(userActivityState).orElse("");
    }

    public Optional<String> applyOpt(final HasUserActivity.UserActivityState userActivityState)
    {
        if(!this.displayActive && userActivityState == HasUserActivity.UserActivityState.ACTIVE)
        {
            return Optional.empty();
        }
        return Optional.of(switch(userActivityState)
        {
            case ACTIVE -> "aktiv";
            case INACTIVE -> "inaktiv";
            case DISABLED -> "deaktiviert";
        });
    }
}

