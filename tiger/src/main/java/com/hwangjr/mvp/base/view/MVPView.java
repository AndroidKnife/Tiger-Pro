package com.hwangjr.mvp.base.view;

import com.hwangjr.mvp.base.presenter.Presenter;

public interface MVPView<P extends Presenter> {
    void setPresenter(P presenter);

    boolean isActive();
}