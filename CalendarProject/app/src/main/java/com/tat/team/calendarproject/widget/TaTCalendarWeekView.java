package com.tat.team.calendarproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tat.team.calendarproject.R;

import java.util.Calendar;

/**
 * Created by kitte on 2016-12-29.
 */

public class TaTCalendarWeekView extends ViewGroup {
    private final int mScreenWidth; ///< 스크린 가로 사이즈
    private final int mWidthDate; ///< 달력의 한 칸 가로 사이즈
    private long mMillis; ///< 달력 시간 데이터
    private int mDefaultTextSize = 40; ///< 기본 텍스트 크기

    public static String[] DAY_OF_WEEK = null; ///< 요일 리스트[일,월,화,수,목,금,토]

    public TaTCalendarWeekView(Context context, AttributeSet attrs) {
        super(context,attrs);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mWidthDate = mScreenWidth / 7;
        DAY_OF_WEEK = getResources().getStringArray(R.array.day_of_week);
    }

    /**
     * 자식 view 사이즈(width/height)를 재 설정하는 함수
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mMillis);

        for(int i=0; i < count; i++) {
            final View child = getChildAt(i);

            if(child.getVisibility() == GONE) continue;

            ///< 자식뷰의 사이즈를 측정
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            ///< 이전 자식뷰 상태를 합쳐서 두 상태의 조합을 반영하는 새로운 정수를 반환
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        // 요일과 일자로 항상 7칸 2줄로 이루어짐(mWidthDate 폰 가로 사이즈 / 7 한 크기)
        maxHeight = (int) (2 * (mWidthDate * 0.75));
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        // 제공된 크기와 모드에 따라 측정 규격을 작성합니다.
        // MEASURED_SIZE_MASK :  getMeasuredWidthAndState()와 getMeasuredWidthAndState()그 실제 측정 된 크기를 제공합니다
        // AT_MOST : 사양 측정 모드 : 그것은 지정된 크기까지 원하는대로 아이로 클 수 있습니다.(wrap_content)
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        /**
         * onMeasure 메소드를 오버라이드 하면, setMeasuredDimension 메소드가 호출이 되지 않으므로, 직접 호출해줘야 함
         * setMeasuredDimension(int width, int height)
         * - width : 가로크기
         * - height : 세로크기
         * resolveSizeAndState(int size, int measureSpec, int childMeasuredState)
         * - size : 뷰 가로 크기
         * - measureSpec : 부모에 의해서 측정된 값
         * - childMeasuredState : 뷰의 자식 크기 정보
         * - MEASURED_HEIGHT_STATE_SHIFT :  MEASURED_STATE_MASK 높이에 도착하는 같은 단일의 int로 폭과 높이를 모두 결합 기능에 대한 비트
         *
         * 여기서는 계산된 크기를 부모 뷰에 맞게 사이즈를 조정하도록 값을 설정하였다.
         */
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, expandSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        // 최종 측정된 높이 값을 레이아웃에 설정한다.
        LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

    }

    /**
     * 뷰가 그 자식들에게 크기와 위치를 할당할 때 호출됩니다.
     * @param b
     * @param i
     * @param i1
     * @param i2
     * @param i3
     */
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        ///< 측정된 가로크기에서 우페딩 값을 뺀 값(실제 가로크기를 구하기 위한 변수)
       final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        ///< 측정된 높이에서 아래페딩 값을 뺀 값(실제 세로크기를 구하기 위한 변수)
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        ///< 가로 전체 길이에서 좌페딩 값을 뺀 값이 실제 가로크기
        final int childWidth = childRight - childLeft;
        ///< 세로 전체 길이에서 위페딩 값을 뺀 값이 실제 세로크기
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int h = 0; h < count; h++) {
            View child = getChildAt(h);

            if (child.getVisibility() == GONE)
                return;

            /**
             * 제공된 크기와 모드에 따라 측정 규격을 작성합니다.
             * childWidth :  계산된 뷰의 가로 크기
             * childHeight : 계산된 뷰의 세로 크기
             * AT_MOST : 사양 측정 모드 : 그것은 지정된 크기까지 원하는대로 아이로 클 수 있습니다.(wrap_content)
             */
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            ///< 실제가로크기 설정
            curWidth = mWidthDate;
            ///< 실제세로크기 설정
            curHeight = (int) (mWidthDate * 0.75);

            ///< 행변경을 위한 조건문
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }

            /**
             * 뷰의 크기 및 위치를 할당하는 메소드 호출
             * 크기를 측정하고 나서 호출
             * laout(int l, int t, int r, int b)
             * - l : 부모기준으로 왼쪽 위치
             * - t : 부모기준으로 위쪽 위치
             * - r : 부모기준으로 오른쪽 위치
             * - b : 부모기준으로 아래 위치
             */
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            ///< 현재 세로크기는 최대 세로크기보다 클수 없음
            if (maxHeight < curHeight) {
                maxHeight = curHeight;
            }

            ///< 뷰를 우측으로 정렬하기 위해서 가로크기 만큼 좌페딩값을 더함
            curLeft += curWidth;
        }
    }

    public void setDate(long millis) {
        mMillis = millis;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private Paint makePaint(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setTextSize(mDefaultTextSize);
        return p;
    }

    /**
     * 선택된 View의 별도의 이벤트를 처리하기 위한 함수
     * - 여기서는 선택된 View의 색을 변경
     * @param view
     */
    public void setCurrentSelectedView(View view) {
        if (getParent() instanceof ViewPager) {
            ViewPager pager = (ViewPager) getParent();
            View tagView = (View) pager.getTag();
            if (tagView != null) {
                long time = (long) tagView.getTag();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);
                for (int i = 0; i < pager.getChildCount(); i++) {
                    for (int j = 0; j < getChildCount(); j++) {
                        TaTCalendarWeekItemView child = (TaTCalendarWeekItemView) ((TaTCalendarWeekView) pager.getChildAt(i)).getChildAt(j);
                        if (child == null) {
                            continue;
                        }
                        if (child.isStaticText()) {
                            continue;
                        }
                        if (child.isSameDay((Long) child.getTag(), (Long) tagView.getTag())) {
                            child.invalidate();
                            break;
                        }
                    }
                }
            }
            if (tagView == view) {
                pager.setTag(null);
                return;
            }
            long time = (long) view.getTag();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            pager.setTag(view);
            view.invalidate();

        }
    }
}
