package com.tao.habaoeat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by wt on 2018/6/21.
 */
class EatView : View {


    val mRandom = Random()

    lateinit var mFoodPosition: Point//食物的位置
    var eatFood = true //食物是否已经被吃掉

    lateinit var mPointList: ArrayList<Point>//主体

    //当前方向 0，1，2，3 上，下，左，右
    val UP = 1
    val DOWN = 2
    val LEFT = 3
    val RIGHT = 4


    lateinit var position: Point
    lateinit var paint: Paint
    var windowX = 0f
    var windowY = 0f
    //宽度
    val eatWid = 20

    var nowDir = 0


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
        windowX = dm.widthPixels.toFloat()
        windowY = dm.heightPixels.toFloat()
        mPointList= ArrayList()
        //起始位置在中心点
        position = Point((windowX / 2).toInt(), (windowY / 2).toInt())
        mFoodPosition=Point()
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        didi()
    }

    /**
     * 开车
     */
    private fun didi() {
        mPointList.add(Point((position.x.toFloat()).toInt(), (position.y.toFloat() - eatWid).toInt()))
        mPointList.add(Point((position.x.toFloat()).toInt(), (position.y)))

    }

    /**
     * 判断方向的方法改为当前方向和触摸范围来判断
     */
    fun xDir(x: Float, y: Float) {
        if (nowDir == UP || nowDir == DOWN) {
            if (x <mPointList[0].x) nowDir = LEFT;
            if (x > mPointList[0].x) nowDir = RIGHT;
        } else {
            if (y < mPointList[0].y) nowDir = UP;
            if (y > mPointList[0].y) nowDir = DOWN;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xDir(event.x, event.y)
            }
        }
        return super.onTouchEvent(event)

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPointList.forEach { point: Point ->
            val rect = Rect(point.x, point.y, point.x + eatWid, point.y + eatWid)
            canvas.drawRect(rect, paint)
        }
        snakeMove(mPointList, nowDir)
        //去掉最后一个
        if (!eatFood) {
            val foodrect = Rect(mFoodPosition.x, mFoodPosition.y, mFoodPosition.x + eatWid, mFoodPosition.y + eatWid)
            val head = mPointList.get(0)
            val headrect = Rect(head.x, head.y, head.x + eatWid, head.y + eatWid)
            if (foodrect.intersect(headrect)) {
                eatFood = true
            } else {
                mPointList.removeAt(mPointList.size - 1)
            }
        } else {
            mPointList.removeAt(mPointList.size - 1)
        }
        drawFood(canvas,paint)
    }

    private fun drawFood(canvas: Canvas, paint: Paint) {
        if (eatFood) {
            mFoodPosition.x = mRandom.nextInt((windowX - 50 - eatWid).toInt())
            mFoodPosition.y = mRandom.nextInt((windowY - 50 - eatWid).toInt())
            eatFood = false
        }
        val food = Rect(mFoodPosition.x, mFoodPosition.y, mFoodPosition.x + eatWid, mFoodPosition.y + eatWid)
        canvas.drawRect(food, paint)

    }

    fun snakeMove(list: ArrayList<Point>, direction: Int) {
        val orighead = list[0]
        val newhead = Point()
        when (direction) {
            UP -> {
                newhead.x = orighead.x
                newhead.y = orighead.y - eatWid
            }
            DOWN -> {
                newhead.x = orighead.x
                newhead.y = orighead.y + eatWid
            }
            LEFT -> {
                newhead.x = orighead.x - eatWid
                newhead.y = orighead.y
            }
            RIGHT -> {
                newhead.x = orighead.x + eatWid
                newhead.y = orighead.y
            }
            else -> {
            }
        }
        adjustHead(newhead)
        list.add(0, newhead)
    }

    private fun isOutBound(point: Point): Boolean {
        if (point.x < 0 || point.x > windowX) return true
        return if (point.y < 0 || point.y > windowY) true else false
    }


    private fun adjustHead(point: Point) {
        if (isOutBound(point)) {
            if (nowDir == UP) point.y = (windowY - eatWid).toInt()
            if (nowDir == DOWN) point.y = (windowY / 2).toInt()
            if (nowDir == LEFT) point.x = (windowX - eatWid).toInt()
            if (nowDir == RIGHT) point.x = (windowX / 2).toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(windowX.toInt(), windowY.toInt())
    }

}
