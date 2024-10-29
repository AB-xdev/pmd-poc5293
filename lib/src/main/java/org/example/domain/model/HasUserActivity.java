package org.example.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public interface HasUserActivity
{
    LocalDateTime getDisabledAt();

    LocalDateTime getLastLoginAt();

    default boolean isDisabled()
    {
        return Optional.ofNullable(this.getDisabledAt())
                .filter(d -> d.isBefore(LocalDateTime.now(ZoneOffset.UTC)))
                .isPresent();
    }

    default boolean isInactive(final Duration inactiveAfter)
    {
        return Optional.ofNullable(this.getLastLoginAt())
                .filter(d -> d.isAfter(getUserInactivityDate(inactiveAfter)))
                .isEmpty();
    }

    default UserActivityState getActivityState(final Duration inactiveAfter)
    {
        if(this.isDisabled())
        {
            return UserActivityState.DISABLED;
        }
        return this.isInactive(inactiveAfter)
                ? UserActivityState.INACTIVE
                : UserActivityState.ACTIVE;
    }

    static LocalDateTime getUserInactivityDate(final Duration inactiveAfter)
    {
        return LocalDateTime.now(ZoneOffset.UTC).minus(inactiveAfter);
    }

    enum UserActivityState
    {
        ACTIVE,
        INACTIVE,
        DISABLED
    }
}
