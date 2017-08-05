package com.princekumar.xyzreader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.princekumar.xyzreader.XYZReaderApp;
import com.princekumar.xyzreader.injections.components.ActivityComponents;
import com.princekumar.xyzreader.injections.components.DaggerActivityComponents;
import com.princekumar.xyzreader.injections.modules.ActivityModule;


/**
 * Created by princ on 05-08-2017.
 */

public class BaseActivity extends AppCompatActivity {

    private ActivityComponents activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (activityComponent == null) {
            activityComponent = DaggerActivityComponents.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponents(XYZReaderApp.get(this).getComponent())
                    .build();
        }
    }

    public ActivityComponents getActivityComponent() {
        return activityComponent;
    }
}
