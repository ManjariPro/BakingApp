package com.propelld.app.bakingapp.idlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import java.util.concurrent.atomic.AtomicBoolean;

public class BakingIdlingResource implements IdlingResource
{
    @Nullable
    private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName()
    {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow()
    {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback)
    {
        mCallback = callback;
    }

    /**
     *
     * @param isIdleNow
     */
    public void setIdleState(boolean isIdleNow)
    {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null)
        {
            mCallback.onTransitionToIdle();
        }
    }
}