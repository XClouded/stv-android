package waveform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.View;

public class WaveFormView  extends View {
	private Bitmap  mBmp = null;
	private final Paint mPaint = new Paint();
    private DataSource mInBuffer = new DataSource();
    
    /**  
     * X轴缩小的比例  
     */  
    public int mRateX = 4;   
    
    /**  
     * Y轴缩小的比例 （为了更精确使用float型）
     */  
    public float mRateY = 4;   
    /**  
     * Y轴基线  
     */  
    public int mBaseLine = 0; 
    
    /**  
     * 初始化  
     */  

	public WaveFormView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}

	public WaveFormView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	 //画曲线，向上的方向
	 private void drawCurveUp(Canvas canvas, float startX, float startY, float stopX, float stopY, Paint paint){
		float lenX = stopX - startX;
		float lenY = stopY - startY;
		if ((Math.abs(lenX) < 1)||(Math.abs(lenY) > 1e-8)) {
			canvas.drawLine(startX, startY, stopX, stopY, mPaint);
		} else {
			canvas.drawLine(startX, startY, startX+lenX/3, stopY-lenX/3, mPaint);
			canvas.drawLine(startX+lenX/3, startY-lenX/3, stopX-lenX/3, stopY-lenX/3, mPaint);
			canvas.drawLine(stopX-lenX/3, startY-lenX/3, stopX, stopY, mPaint);
		}
	 }
	 
	 //画曲线，向下的方向
	 private void drawCurveDw(Canvas canvas, float startX, float startY, float stopX, float stopY, Paint paint){
			float lenX = stopX - startX;
			float lenY = stopY - startY;
			if ((Math.abs(lenX) < 1)||(Math.abs(lenY) > 1e-8)) {
				canvas.drawLine(startX, startY, stopX, stopY, mPaint);
			} else {
				canvas.drawLine(startX, startY, startX+lenX/3, stopY+lenX/3, mPaint);
				canvas.drawLine(startX+lenX/3, startY+lenX/3, stopX-lenX/3, stopY+lenX/3, mPaint);
				canvas.drawLine(stopX-lenX/3, startY+lenX/3, stopX, stopY, mPaint);
			}
		 }
	 
	 //绘制波形图
	 public boolean initBitmap(int w,int h,int c){
		 
		 mBmp = Bitmap.createBitmap(w,h, Config.ARGB_8888);
			
			Canvas canvas = new Canvas(mBmp); 
			if(canvas == null)
				return false;
			int viewWidth = this.getWidth();
			int viewHeight = this.getHeight();
			
	        int width = viewWidth/mRateX;
	        mBaseLine = viewHeight/2;
	        
	        short[] bufferMax = new short[width];
	        getSplitMaxBuffer(bufferMax, width);	        
			
			canvas.drawColor(Color.DKGRAY);// 清除背景   	        
	        int x0,x1,x2,y0,y1,y2,y; 
	        for (int i = 0; i < width; i += 1) { 
	            y = (int)((float)(bufferMax[i] / mRateY) + 0.5)+1;// 调节缩小比例，四舍五入
	            y0 = mBaseLine-y; y1 = mBaseLine; y2 = mBaseLine+y; 
	            x0 = i*mRateX; x1 = i*mRateX + (mRateX/2); x2 = i*mRateX + mRateX; 
	            //竖线
	            canvas.drawLine(x0, y1, x0,  y0, mPaint);	                       
	            canvas.drawLine(x1, y0, x1,  y2, mPaint);
	            canvas.drawLine(x2, y1, x2,  y2, mPaint);
	            
	            //曲线
	            drawCurveUp(canvas, x0, y0, x1,  y0, mPaint);
	            drawCurveDw(canvas, x1, y2, x2,  y2, mPaint);        
	        }   
	        return true;	 
	 }

    /**  
     * 初始化 ： x轴y轴缩小比率
     */  
    public void initRate(int rateX, float rateY) {   
       this.mRateX = rateX;   
       this.mRateY = rateY; 
    } 
    
    //画笔初始化
	private void initPaint() {
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GRAY);// 画笔为灰色
		mPaint.setStrokeWidth(2);// 设置画笔粗细
	}
    
	//清除
    public void clear() {
    	mInBuffer.len = 0;
    }

    //将mInBuffer分割为width个小段，提取每小段的最大值
    void getSplitMaxBuffer(short[] bufferMax, int width){
    	int len = mInBuffer.len;
    	
    	for(int j= 0; j<width; j++)	{
    		bufferMax[j] = 0;
    	}
    	
    	if(len<=1)
    		return;
    	
    	int idxVolu = 0;
    	int lastIdx = 0;
    	int volIdx = 0;
    	for(int i = 0; i< width; i++){
    		idxVolu = i*(len-1)/(width-1);
    		
    		for(volIdx= lastIdx; volIdx<=idxVolu; volIdx++){
    			if(volIdx >= mInBuffer.len)
    				break;
    			if(bufferMax[i]<mInBuffer.values[volIdx])
    				bufferMax[i] = mInBuffer.values[volIdx];    			
    		}
    		
    		lastIdx = volIdx;    	
    	}

 }

    //为绘制波形图添加源数据
	public void addBuffer(DataSource data) {
		mInBuffer.values = new short[data.len];
		mInBuffer.len = data.len;

		for (int i = 0; i < data.len; i++) {
			mInBuffer.values[i] = data.values[i];
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initPaint();
		if (mBmp != null) {
			Matrix matrix = new Matrix();
			matrix.setRotate(0, 120, 120);
			canvas.drawBitmap(mBmp, matrix, mPaint);
		}
	}
}
	
