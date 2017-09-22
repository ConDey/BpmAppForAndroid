package com.eazytec.bpm.appstub.resourcesloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * @author Beckett_W
 * @version Id: BpmResourcesLoader, v 0.1 2017/9/20 8:22 Beckett_W Exp $$
 */
public class BpmResourcesLoader implements ResourcesConstant {

    private static BpmResourcesLoader resourcesLoader;
    private Context context;

    private BpmResourcesLoader(Context context) {
        this.context = context;
    }

    /**
     * 获得默认的资源属性加载器
     *
     * @param context
     * @return
     */
    public static BpmResourcesLoader getBpmResourcesLoader(Context context) {
        if (resourcesLoader == null) {
            resourcesLoader = new BpmResourcesLoader(context);
        }
        return resourcesLoader;
    }

    /**
     * 根据布局名称获取布局ID信息
     *
     * @param layoutName
     * @return
     */
    public int getLayoutID(String layoutName) {
        int layoutID = context.getResources().getIdentifier(layoutName, RESOURCES_LAYOUT_NAME, context.getPackageName());
        return layoutID;
    }

    /**
     * 根据控件名称获取控件ID
     *
     * @param widgetName
     * @return
     */
    public int getWidgetID(String widgetName) {
        int widgetID = context.getResources().getIdentifier(widgetName, RESOURCES_ID_NAME, context.getPackageName());
        return widgetID;
    }


    /**
     * 根据菜单名称获取菜单ID
     *
     * @param menuName
     * @return
     */
    public int getMenuID(String menuName) {
        int menuID = context.getResources().getIdentifier(menuName, RESOURCES_MENU_NAME, context.getPackageName());
        return menuID;
    }

    /**
     * 根据动画名称获取动画ID
     *
     * @param animName
     * @return
     */
    public int getAnimID(String animName) {
        return context.getResources().getIdentifier(animName, RESOURCES_ANIM_NAME, context.getPackageName());
    }

    /**
     * 根据资源名称获取资源ID
     *
     * @param rawName
     * @return
     */
    public int getRawID(String rawName) {
        return context.getResources().getIdentifier(rawName, RESOURCES_RAW_NAME, context.getPackageName());
    }


    public int getDrawableID(String drawableName) {
        return context.getResources().getIdentifier(drawableName, RESOURCES_DRAWABLE_NAME, context.getPackageName());
    }

    @TargetApi(21)
    public Drawable getDrawable(String drawableName) {
        int drawableID = getDrawableID(drawableName);

        if (Build.VERSION.SDK_INT  >= 21) {
            return context.getResources().getDrawable(drawableID, null);
        }
        return context.getResources().getDrawable(drawableID);
    }

    /**
     * 根据attrName获取自定义属性ID
     *
     * @param attrName
     * @return
     */
    public int getAttrID(String attrName) {
        return context.getResources().getIdentifier(attrName, RESOURCES_ATTR_NAME, context.getPackageName());
    }

    /**
     * 根据dimenName获取字体ID
     *
     * @param dimenName
     * @return
     */
    public int getDimenID(String dimenName) {
        return context.getResources().getIdentifier(dimenName, RESOURCES_DIMEN_NAME, context.getPackageName());
    }

    /**
     * 根据StrName获得StringID
     *
     * @param strName
     * @return
     */
    public int getStringID(String strName) {
        return context.getResources().getIdentifier(strName, RESOURCES_STRING_NAME, context.getPackageName());
    }

    /**
     * 获得String的值
     *
     * @param strName
     * @return
     */
    public String getString(String strName) {
        return context.getResources().getString(getStringID(strName));
    }


    /**
     * 获得app_name的名字
     *
     * @return
     */
    public String getAppNameString() {
        return getString("app_name");
    }

    /**
     * 根据ColorName获得ColorID
     *
     * @param colorName
     * @return
     */
    public int getColorID(String colorName) {
        return context.getResources().getIdentifier(colorName, RESOURCES_COLOR_NAME, context.getPackageName());
    }

    /**
     * 获得颜色代码
     *
     * @param colorName
     * @return
     */
    @TargetApi(23)
    public int getColor(String colorName) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(getColorID(colorName), null);
        }
        return context.getResources().getColor(getColorID(colorName));
    }

    /**
     * 获得主题代码
     *
     * @param styleName
     * @return
     */
    public int getStyleID(String styleName) {
        return context.getResources().getIdentifier(styleName, RESOURCES_STYLE_NAME, context.getPackageName());
    }

    /**
     * 获得integer-array元素
     *
     * @param strName
     * @return
     */
    public int[] getInteger(String strName) {
        return context.getResources().getIntArray(context.getResources().getIdentifier(strName, RESOURCES_ARRAY_NAME,
                context.getPackageName()));
    }
}

