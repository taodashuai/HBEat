package com.tao.habaoeat

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View

/**
 * Created by wt on 2018/6/21.
 */
class EatView : View {

    /**
     * 当前头坐标
     */
    lateinit var position: Point
    lateinit var paint: Paint
    var windowX = 0
    var windowY = 0
    //宽度
    val eatWid = 20

    //当前方向 0，1，2，3 上，下，左，右
    var nowDir = 0
    var nextDir = 0
    //当前速度
    var nowSpeed = 20
    //当前长度
    var nowLength = 100
    var bodyLists = ArrayList<Point>()


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)

    }


    fun init(context: Context) {
        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        windowX = dm.widthPixels
        windowY = dm.heightPixels
        //起始位置在中心点
        position = Point(windowX / 2, windowY / 2)
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        didi()
    }

    /**
     * 开车
     */
    private fun didi() {
        bodyLists.add(Point((position.x.toFloat() - eatWid / 2).toInt(), (position.y.toFloat() - nowLength).toInt()))
        bodyLists.add(Point((position.x.toFloat() + eatWid / 2).toInt(), (position.y)))

//        val sec: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
//        sec.scheduleAtFixedRate({
//            post { invalidate() }
//        }, 1000, 1000, TimeUnit.MILLISECONDS)
    }

    /**
     * x法判断上下左右方向
     */
    fun xDir(event: MotionEvent): Int {
        if (event.y / event.x < windowY / windowX) {
            //左上到右下线的右侧
            if ((windowY - event.y) / event.x < windowY / windowX) {
                return 3
            } else {
                return 1
            }
        } else {
            if ((windowY - event.y) / event.x < windowY / windowX) {
                return 0
            } else {
                return 2
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //检测到按下操作，确定当前方向，怎样判断呢，以X方式来判断
                if (nowDir != xDir(event)) {
                    //准备转弯
                    nextDir = xDir(event)
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bodyLists[1].x - bodyLists[0].x == 20) {
            bodyLists[0].y += nowSpeed
        }
        when (nextDir) {
            0 -> {
            }
            1 -> {

            }
            2 -> {

            }
            3 -> {
                bodyLists[bodyLists.size-1].x += nowSpeed
            }
        }
        for (n in 0..bodyLists.size - 2) {
            canvas.drawRect(bodyLists[n].x.toFloat(), bodyLists[n].y.toFloat(), bodyLists[n + 1].x.toFloat(), bodyLists[n + 1].y.toFloat(), paint)
        }

//        when (nowDir) {
//            0 -> {
//
//                position.y += nowSpeed
//            }
//            3 -> {
//
//            }
//
//        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(windowX, windowY);

    }


}
