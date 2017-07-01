package com.eazytec.bpm.applaunch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.eazytec.bpm.R;

import net.wequick.small.Small;

/**
 * full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @author ConDey
 * @version Id: AppLaunchActivity, v 0.1 2017/6/26 下午2:33 ConDey Exp $$
 */
public class AppLaunchActivity extends Activity {

    private ParticleView appLaunchPView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_launch);
        appLaunchPView = (ParticleView) findViewById(R.id.pv_applaunch);
    }

    @Override
    protected void onStart() {
        super.onStart();

        appLaunchPView.setmParticleText(AppInfo.getVersionName(getApplicationContext()));
        appLaunchPView.setmHostText(AppInfo.getAppName(getApplicationContext()));
        appLaunchPView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Small.openUri("app.home", AppLaunchActivity.this);
                finish();
            }
        });

        Small.setUp(AppLaunchActivity.this, new net.wequick.small.Small.OnCompleteListener() {
            @Override
            public void onComplete() {
                appLaunchPView.startAnim();
            }
        });


    }


}
