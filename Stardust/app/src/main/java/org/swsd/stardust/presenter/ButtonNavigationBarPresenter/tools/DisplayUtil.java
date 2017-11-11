package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools;

import android.content.Context;

/**
 *     author : 熊立强
 *     time : 2017/11/11
 *     description :
 *     version : 1.0
 */
public class DisplayUtil {

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
        }

        }
