package com.zbxn.main.widget.calendar.bizs.themes;

/**
 * 主题的默认实现类
 * <p>
 * The default implement of theme
 *
 * @author ZSS 2015-06-17
 */
public class DPBaseTheme extends DPTheme {
    @Override
    public int colorBG() {
        return 0xFFFFFFFF;
    }

    @Override
    public int colorBGCircle() {
        return 0xFF2B96DA;//系统蓝色 R.colors.main_tab_text_blue
    }

    @Override
    public int colorTitleBG() {
        return 0xFFF37B7A;
    }

    @Override
    public int colorTitle() {
        return 0xEEFFFFFF;
    }

    @Override
    public int colorToday() {//今天
        return 0xFF2B96DA;//系统蓝色 R.colors.main_tab_text_blue
    }

    @Override
    public int colorG() {//普通工作日
        return 0xEE666666;
    }

    @Override
    public int colorF() {//农历节日
//        return 0xEEC08AA4;
        return 0xEE888888;
    }

    @Override
    public int colorWeekend() {//周末
        return 0xFF999999;//灰色
    }

    @Override
    public int colorHoliday() {//节日
        return 0xEE666666;
    }

    @Override
    public int colorTodayText() {//今天
        return 0xFF2B96DA;//系统蓝色 R.colors.main_tab_text_blue
    }

    @Override
    public int colorChooseText() {//选中的日期
        return 0x00000000;
    }

    @Override
    public int colorChooseDot() {//选中的日期点颜色
        return 0xFFFFFFFF;
    }

    @Override
    public int colorUnChooseDot() {//未选中的日期点颜色
        return 0xFF2B96DA;//系统蓝色 R.colors.main_tab_text_blue
    }
}
