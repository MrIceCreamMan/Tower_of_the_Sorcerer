package com.mybringback.thebasics;

import android.app.Activity;

public class TutorialFour extends Acticity {

	DrawingTheBall v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		v = new DrawingTheBall(this);
		setContentView(v);
	}
}


//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

package com.mybringback.thebasics;

import android.content.Context;
import android.graphics,Rect;
import android.view.View;

public class DrawingTheBall extends View{

	Bitmap bball;
	int x,y;
	
	public DrawingTheBall(Context context) {
		super(context);
		bball = BitmapFactory.decodeResource(getResources(),R.drawable.blueball);
		x = 0;
		y = 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect ourRect = new Rect();
		ourRect.set(0, 0, canvas.getWidth(), canvas.getHeight()/2);

		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		blue.setStyle(Paint.Style.FILL);

		canvas.drawRect(ourRect, blue);

		if (x < canvas.getWidth()){
			x += 10;
		} else {
			x = 0;
		}
		if (y < canvas.getHeight()){
			y += 10;
		} else {
			y = 0;
		}
		Paint p = new Paint();
		canvas.drawBitmap(bball, x, y, p);
		invalidate();
	}


}

//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

package com.mybringback.thebasics;

import android.app.Activity;

public class SurfaceViewExample extends Activity implements OnTouchListener {

	OurView v;
	Bitmap ball, blob;
	float x, y;
	Sprite sprite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		v = new OurView(this);
		v.setOnTouchListener(this);
		ball = BitmapFactory.decodeResource(getResources(), R.drawable.blueball);
		blob = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet);
		x = y = 0;
		setContentView(v);
	}

	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}

	public class OurView extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;

		public OurView(Context context) {
			super(context);
			holder = getHolder();
		}

		public void run() {

			sprite = new Sprite(Ourview.this, blob);

			while (isItOK == true) {
				if (!holder.getSurface().isValid()) {
					continue;
				}
				Canvas c = holder.lockCanvas();
				onDraw(c);
				holder.unlockCanvasAndPost(c);
			}

		}

		protected void onDraw(Canvas canvas) {
			canvas.drawARGB(255, 150, 150, 10);
			canvas.drawBitmap(ball, x - ball.getWidth()/2, y - ball.getHeight()/2, null);
			sprite.onDraw(canvas);
		}

		public void pause() {
			isItOK = false;
			while(true) {
				try{
					t.join();
				} catch ( InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
	}

	public boolean onTouch(View v, MotionEvent me) {

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		switch(me.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = me.getX();
				y = me.getY();
				break;
			case MotionEvent.ACTION_UP:
				x = me.getX();
				y = me.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				x = me.getX();
				y = me.getY();
				break;
		}

		return true;
	}

}

//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

package com.mybringback.thebasics;

import com.mybringback.thebasics.SurfaceViewExample.OurView;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {

	int x, y;
	int xSpeed, ySpeed;
	int height, width;
	Bitmap b;
	OurView ov;
	int currentFrame = 0;
	int direction = 3;

	public Sprite(OurView ourView, Bitmap blob){
		b = blob;
		ov = ourView;
		height = b.getHeight() / 4;
		width = b.getWidth() / 4;
		x = y =0;
		xSpeed = 5;
		ySpeed = 0;
	}

	private void update() {

		// 0 = up
		// 1 = down
		// 2 = left
		// 3 = right

		// facing down
		if (x > ov.getWidth() - width - xSpeed){
			xSpeed = 0;
			ySpeed = 5;
			direction = 1;
		}
		//going left
		if ( y > ov.getHeight() - height - ySpeed) {
			xSpeed = -5;
			ySpeed = 0;
			direction = 2;
		}
		// facing up
		if (x + xSpeed < 0) {
			x = 0;
			xSpeed = 0;
			ySpeed = -5;
			direction = 0;
		}
		//facing right
		if (y + ySpeed < 0) {
			y = 0;
			xSpeed = 5;
			ySpeed = 0;
			direction = 3;
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		currentFrame = ++currentFrame % 4;
		x += xSpeed;
		y += ySpeed;
	}

	public void onDraw(Canvas canvas){
		update();
		int srcX = currentFrame * width; 
		int srcY = direction * height;
		Rect src = new Rect (srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(b, src, dst, null);
	}

}


//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (editText) findViewByID(R.id.edittext);
		textView = (textView) findViewByID(R.id.textview);
	}


	public void writeMessage(View view) {
		String Message = eidtText.getText().toString();
		String file_name = "hello_file.txt";
		try {
			FineOutputStream fileOUtputStream = openFileOutput(file_name.MODE_PRIVATE);
			fileOutputStream.write(Message.getBytes());
			fileOutputStream.close();
			Toast.makeText(getApplicationContext(),"Message saved", Toast.LENGTH_LONG).show();
			editText.setText("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} cat (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMessage(View view) {
		try {
			String Message;
			FileInputStream fileInputStream = openFileInput("hello_file.txt");
			InputStreamReader InputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer stringbuffer = new StringBuffer();
			while (Message = bufferedReader.readLine()!= null) {
				stringBuffer.append(Message + "\n");
			}
			textView.setText(stringbuffer.toString());
			textview.setVisibility(View.VISIBLE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

