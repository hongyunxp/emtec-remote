package com.sjarliecodes.emtecremote;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalView extends View {
	
	public enum eButton
	{
		NONE,
		UP,
		LEFT,
		DOWN,
		RIGHT,
		OK,
		MUTE,
		INFO,
		REWIND,
		FORWARD,
		PREVIOUS,
		STOP,
		PLAYPAUSE,
		RECORD,
		NEXT,
		ZERO,
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		RED,
		GREEN, 
		YELLOW,
		BLUE,
		HOME,
		MENU,
		BACK
	};
	private OnClickListener m_listener;
	
	public interface OnClickListener {
		void onClick (View i_view, eButton i_button);
	}
	
	private Resources m_res;
	private Drawable m_background;
	private Drawable m_backgroundMask;
	private Drawable m_backgroundTopButtons;
	private Drawable m_backgroundNumericSlider;
	private Drawable m_backgroundColorButtons;
	private Drawable m_backgroundMenu;
	private Drawable m_foregroundTopButtons;
	private Drawable m_foregroundNumericSlider;
	private Drawable m_foregroundColorButtons;
	private Drawable m_foregroundMenu;
	private Drawable m_foregroundNrsEightToTwo;
	private Drawable m_foregroundNrsThreeToSeven;
	private Drawable m_activeButtonDraw;
	
	private Drawable m_trialOverlay;
	
	private Paint m_textPaint;
	private int m_textPaddingLeft;
	private int m_textPaddingBottom;
	
	private int m_width; 				// width of button surface
	private int m_offsetWidth; 			// offset of button surface from left screen border
	private float m_sliderPosition;
	private float m_sliderPreviousX;
	private float m_numberSize;
	
	private int m_controlButtonsTop;
	private int m_controlButtonsHeight;
	private int m_numericSliderTop;
	private int m_numericSliderHeight;
	private int m_colorButtonsTop;
	private int m_colorButtonsHeight;
	private int m_menuButtonsTop;
	private int m_menuButtonsHeight;
	
	private eButton m_activeButton;
	
	public VerticalView (Context context) {
		super(context);
		
		initializeButtons (context);
	}
	
	public VerticalView (Context context, AttributeSet attrs)
	{
		super (context, attrs);
				
		initializeButtons (context);
	}
	
	public void setOnClickListener (OnClickListener i_listener)
	{
		m_listener = i_listener;
	}
	
	private void initializeButtons (Context context)
	{
		m_activeButton = eButton.NONE;
		m_sliderPosition = 0;
		
		m_res = context.getResources ();
		m_background = m_res.getDrawable (R.drawable.bg_color_px_iss2);
		m_backgroundMask = m_res.getDrawable (R.drawable.bg_color_px_iss2);
		m_backgroundTopButtons = m_res.getDrawable (R.drawable.control_buttons_bg_iss3);
		m_backgroundNumericSlider = m_res.getDrawable (R.drawable.slider_bg_iss2);
		m_backgroundColorButtons = m_res.getDrawable (R.drawable.color_buttons_bg_iss2);
		m_backgroundMenu = m_res.getDrawable (R.drawable.menu_buttons_bg_iss2);
		m_foregroundTopButtons = m_res.getDrawable (R.drawable.control_buttons_fg_iss2);
		m_foregroundNumericSlider = m_res.getDrawable (R.drawable.slider_fg_iss3);
		m_foregroundColorButtons = m_res.getDrawable (R.drawable.color_buttons_fg_iss3);
		m_foregroundMenu = m_res.getDrawable (R.drawable.menu_buttons_fg_iss1);
		m_foregroundNrsEightToTwo = m_res.getDrawable (R.drawable.slider_numbers_8to2);
		m_foregroundNrsThreeToSeven = m_res.getDrawable (R.drawable.slider_numbers_3to7);
		m_activeButtonDraw = m_res.getDrawable (R.drawable.highlight_color_px);
		
		m_trialOverlay = m_res.getDrawable (R.drawable.trial_overlay);
		
		m_textPaint = new Paint ();
		m_textPaint.setColor (Color.WHITE);
		m_textPaint.setStyle(Paint.Style.FILL);
		m_textPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{	
		// draw background
		m_background.draw(canvas);
		m_backgroundTopButtons.draw(canvas);
		if (m_colorButtonsTop > 0)
		{
			m_backgroundColorButtons.draw(canvas);
		}
		m_backgroundMenu.draw(canvas);
		
		int left = 0, top = 0, right = 0, bottom = 0;
		String number = "";
		
		// draw active button
		switch (m_activeButton)
		{
		case UP:
			left = m_offsetWidth + (int)(0.375*m_width);
			top = m_controlButtonsTop;
			right = left + (int)(0.25*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case LEFT:
			left = m_offsetWidth + (int)(0.1*m_width);
			top = m_controlButtonsTop + (int)(0.2*m_width);
			right = left + (int)(0.25*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case DOWN:
			left = m_offsetWidth + (int)(0.375*m_width);
			top = m_controlButtonsTop + (int)(0.4*m_width);
			right = left + (int)(0.25*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case RIGHT:
			left = m_offsetWidth + (int)(0.65*m_width);
			top = m_controlButtonsTop + (int)(0.2*m_width);
			right = left + (int)(0.25*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case OK:
			left = m_offsetWidth + (int)(0.4*m_width);
			top = m_controlButtonsTop + (int)(0.15*m_width);
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			break;
		case MUTE:
			left = m_offsetWidth + (int)(0.1*m_width);
			top = m_controlButtonsTop;
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.15*m_width);
			break;
		case INFO:
			left = m_offsetWidth + (int)(0.7*m_width);
			top = m_controlButtonsTop;
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.15*m_width);
			break;
		case REWIND:
			left = m_offsetWidth + (int)(0.1*m_width);
			top = m_controlButtonsTop + (int)(0.35*m_width);
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.15*m_width);
			break;
		case FORWARD:
			left = m_offsetWidth + (int)(0.7*m_width);
			top = m_controlButtonsTop + (int)(0.35*m_width);
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.15*m_width);
			break;
		case PREVIOUS:
			left = m_offsetWidth + (int)(0.1*m_width);
			top = m_controlButtonsTop + (int)(0.6*m_width);
			right = left + (int)(0.15*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case STOP:
			left = m_offsetWidth + (int)(0.25*m_width);
			top = m_controlButtonsTop + (int)(0.6*m_width);
			right = left + (int)(0.15*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case PLAYPAUSE:
			left = m_offsetWidth + (int)(0.4*m_width);
			top = m_controlButtonsTop + (int)(0.55*m_width);
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			break;
		case RECORD:
			left = m_offsetWidth + (int)(0.6*m_width);
			top = m_controlButtonsTop + (int)(0.6*m_width);
			right = left + (int)(0.15*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case NEXT:
			left = m_offsetWidth + (int)(0.75*m_width);
			top = m_controlButtonsTop + (int)(0.6*m_width);
			right = left + (int)(0.15*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case RED:
			left = m_offsetWidth + (int)(0.1*m_width);
			top = m_colorButtonsTop;
			right = left + (int)(0.1625*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case GREEN:
			left = m_offsetWidth + (int)(0.3125*m_width);
			top = m_colorButtonsTop;
			right = left + (int)(0.1625*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case YELLOW:
			left = m_offsetWidth + (int)(0.525*m_width);
			top = m_colorButtonsTop;
			right = left + (int)(0.1625*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case BLUE:
			left = m_offsetWidth + (int)(0.7375*m_width);
			top = m_colorButtonsTop;
			right = left + (int)(0.1625*m_width);
			bottom = top + (int)(0.1*m_width);
			break;
		case HOME:
			left = m_offsetWidth + (int)(0.15*m_width);
			top = m_menuButtonsTop;
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			break;
		case MENU:
			left = m_offsetWidth + (int)(0.4*m_width);
			top = m_menuButtonsTop;
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			break;
		case BACK:
			left = m_offsetWidth + (int)(0.65*m_width);
			top = m_menuButtonsTop;
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			break;
		case ZERO:
			number = "0";
			break;
		case ONE:
			number = "1";
			break;
		case TWO:
			number = "2";
			break;
		case THREE:
			number = "3";
			break;
		case FOUR:
			number = "4";
			break;
		case FIVE:
			number = "5";
			break;
		case SIX:
			number = "6";
			break;
		case SEVEN:
			number = "7";
			break;
		case EIGHT:
			number = "8";
			break;
		case NINE:
			number = "9";
			break;
		default:
			left = 0;
			top = 0;
			right = 0;
			bottom = 0;
			break;
		}
		
		if (left + top + right + bottom > 0)
		{
			m_activeButtonDraw.setBounds(left, top, right, bottom);
			m_activeButtonDraw.draw (canvas);
		}
		
		// slider
		if (m_numericSliderTop > 0)
		{
			m_backgroundNumericSlider.draw(canvas);
			
			left = m_offsetWidth + (int)(0.1 * m_width + m_sliderPosition);
			top = m_numericSliderTop;
			right = (int)(left + 0.8 * m_width);
			bottom = (int)(top + 0.15 * m_width);
			m_foregroundNrsEightToTwo.setBounds (left, top, right, bottom);
			
			if (m_sliderPosition == 0)
			{
				m_foregroundNrsEightToTwo.draw (canvas);
			}
			else
			{
				if (m_sliderPosition > 0)
				{
					right = left;
					left = (int)(left - 0.8 * m_width);
				}
				else
				{
					left = right;
					right = (int)(right + 0.8 * m_width);
				}
				
				m_foregroundNrsThreeToSeven.setBounds(left, top, right, bottom);
				m_foregroundNrsEightToTwo.draw (canvas);
				m_foregroundNrsThreeToSeven.draw (canvas);
			}
			
			m_backgroundMask.setBounds(0, m_numericSliderTop, (int) (m_offsetWidth + 0.1 * m_width), bottom);
			m_backgroundMask.draw(canvas);
			m_backgroundMask.setBounds((int) (m_offsetWidth + m_width - 0.1 * m_width), m_numericSliderTop, 2 * m_offsetWidth + m_width, bottom);
			m_backgroundMask.draw(canvas);
			
			m_foregroundNumericSlider.draw(canvas);
		}
		
		if (m_colorButtonsTop > 0)
		{
			m_foregroundColorButtons.draw(canvas);
		}
		
		m_foregroundTopButtons.draw(canvas);
		m_foregroundMenu.draw(canvas);
		
		// number on OK button
		if (number != "")
		{
			left = m_offsetWidth + (int)(0.4*m_width);
			top = m_controlButtonsTop + (int)(0.15*m_width);
			right = left + (int)(0.2*m_width);
			bottom = top + (int)(0.2*m_width);
			m_activeButtonDraw.setBounds(left, top, right, bottom);
			m_activeButtonDraw.draw (canvas);
			
			canvas.drawText (number, left + m_textPaddingLeft, bottom - m_textPaddingBottom, m_textPaint);
		}
		
		// trial only
		m_trialOverlay.draw (canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// determine the width height ratio
		// => our layout will have a 1.5 height/width ratio
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		double ratio = ((double)height) / width;
		
		// determine the viewed elements
		if (ratio < 1.35)
		{
			// only top buttons and menu (height = 0.75 and 0.2 x width respectively)			
			if (ratio >= 1.15)
			{
				//  ^ ^
				//  | | controlButtonsTop (min value = 0.1 x width)
				//  | ×
				//  | | controlButtonsHeight = 0.75 x width
				//  | v
				//  | menuButtonsTop = 2 x controlButtonsTop + controlButtonsHeight
				//  ×
				//  | menuButtonsHeight = 0.2 x width
				//  v
				
				m_width = width;
				m_offsetWidth = 0;
				m_controlButtonsTop = (int)((height - 0.95 * m_width) / 2);
				m_controlButtonsHeight = (int)(0.75 * m_width);
			}
			else
			{
				// offsetWidth > 0, use ratio 1.15
				m_width = (int) (height / 1.15);
				m_offsetWidth = (width - m_width) / 2;
				m_controlButtonsTop = (int)(0.1 * m_width);
				m_controlButtonsHeight = (int)(0.75 * m_width);
			}
			
			m_numericSliderTop = -1;
			m_numericSliderHeight = -1;
			m_colorButtonsTop = -1;
			m_colorButtonsHeight = -1;
			m_menuButtonsTop = 2 * m_controlButtonsTop +  m_controlButtonsHeight;
			m_menuButtonsHeight = (int)(0.2 * m_width);
			
			m_numberSize = -1.0f;
		}
		else if (ratio < 1.5)
		{
			// only top buttons, slider and menu
			
			//  ^ ^ ^
			//  | | | controlButtonsTop
			//  | | ×
			//  | | | controlButtonsHeight = 0.75 x width
			//  | | v
			//  | | numericSliderTop = 2 x controlButtonsTop + controlButtonsHeight
			//  | x
			//  | | m_numericSliderHeight = 0.15 x width
			//  | v
			//  | menuButtonsTop = controlButtonsTop + numericSliderTop + numericSliderHeight
			//  ×
			//  | m_menuButtonsHeight = 0.2 x width
			//  v
			
			m_width = width;
			m_offsetWidth = 0;
			m_controlButtonsTop = (int)((height - 1.1 * m_width) / 3);
			m_controlButtonsHeight = (int)(0.75 * m_width);
			
			m_numericSliderTop = 2 * m_controlButtonsTop + m_controlButtonsHeight;
			m_numericSliderHeight = (int)(0.15 * m_width);
			m_colorButtonsTop = -1;
			m_colorButtonsHeight = -1;
			m_menuButtonsTop = (int)(m_controlButtonsTop + m_numericSliderTop + m_numericSliderHeight);
			m_menuButtonsHeight = (int)(0.2 * m_width);
			
			m_numberSize = (float) (0.8 * m_width / 5);
		}
		else
		{
			// only top buttons, slider and menu
			//  ^ ^ ^ ^
			//  | | | | controlButtonsTop
			//  | | | ×
			//  | | | | controlButtonsHeight = 0.75 x width
			//  | | | v
			//  | | | numericSliderTop = 2 x controlButtonsTop + controlButtonsHeight
			//  | | ×
			//  | | | m_numericSliderHeight = 0.15 x width
			//  | | v
			//  | | colorButtonsTop = controlButtonsTop + numericSliderTop + numericSliderHeight
			//  | ×
			//  | | colorButtonsHeight = 0.1 x width
			//  | v
			//  | menuButtonsTop = controlButtonsTop + colorButtonsTop + colorButtonsHeight + 0.1 * width
			//  ×
			//  | m_menuButtonsHeight = 0.2 x width
			//  v			
			
			m_width = width;
			m_offsetWidth = 0;
			m_controlButtonsTop = (int)((height - 1.2 * m_width) / 4);
			m_controlButtonsHeight = (int)(0.75 * m_width);
			
			m_numericSliderTop = 2 * m_controlButtonsTop + m_controlButtonsHeight;
			m_numericSliderHeight = (int)(0.15 * m_width);
			m_colorButtonsTop = m_controlButtonsTop + m_numericSliderTop + m_numericSliderHeight;
			m_colorButtonsHeight = (int)(0.1 * m_width);
			m_menuButtonsTop = m_controlButtonsTop + m_colorButtonsTop + m_colorButtonsHeight;
			m_menuButtonsHeight = (int)(0.2 * m_width);
			
			m_numberSize = (float) (0.8 * m_width / 5);
		}
		
		m_textPaint.setTextSize((float) (0.2 * m_width));
		m_textPaddingBottom = (int)(0.03 * m_width);
		m_textPaddingLeft = (int)(0.045 * m_width);
		
		m_background.setBounds (0, 0, m_width + 2*m_offsetWidth, m_menuButtonsTop + m_menuButtonsHeight);
		
		m_backgroundTopButtons.setBounds (m_offsetWidth, m_controlButtonsTop, m_offsetWidth + m_width, m_controlButtonsTop + m_controlButtonsHeight);
		m_foregroundTopButtons.setBounds (m_offsetWidth, m_controlButtonsTop, m_offsetWidth + m_width, m_controlButtonsTop + m_controlButtonsHeight);
		if (m_numericSliderHeight > 0)
		{
			m_backgroundNumericSlider.setBounds (m_offsetWidth, m_numericSliderTop, m_offsetWidth + m_width, m_numericSliderTop + m_numericSliderHeight);
			m_foregroundNumericSlider.setBounds (m_offsetWidth, m_numericSliderTop, m_offsetWidth + m_width, m_numericSliderTop + m_numericSliderHeight);
		}
		if (m_colorButtonsHeight > 0)
		{
			m_backgroundColorButtons.setBounds (m_offsetWidth, m_colorButtonsTop, m_offsetWidth + m_width, m_colorButtonsTop + m_colorButtonsHeight);
			m_foregroundColorButtons.setBounds (m_offsetWidth, m_colorButtonsTop, m_offsetWidth + m_width, m_colorButtonsTop + m_colorButtonsHeight);
		}
		m_backgroundMenu.setBounds (m_offsetWidth, m_menuButtonsTop, m_offsetWidth + m_width, m_menuButtonsTop + m_menuButtonsHeight);
		m_foregroundMenu.setBounds (m_offsetWidth, m_menuButtonsTop, m_offsetWidth + m_width, m_menuButtonsTop + m_menuButtonsHeight);
		
		m_trialOverlay.setBounds (0, 0, width, height);
		
		// very important: must call this method!!
		setMeasuredDimension (width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			// find out where the action was
			double x = (event.getX () - m_offsetWidth) / m_width;
			double y = event.getY ();
			
			// slider region
			if (y >= m_numericSliderTop && y <= m_numericSliderTop + m_numericSliderHeight)
			{
				// adjust slider position
				m_sliderPosition = m_sliderPosition + (event.getX () - m_sliderPreviousX);
				
				// calculate selected number
				int selectedNumber = (int) ((m_numberSize / 2 - m_sliderPosition) / (m_numberSize) + 10) % 10;
				
				m_sliderPosition = -selectedNumber * m_numberSize;
				if (m_sliderPosition > 0.8 * m_width)
				{
					m_sliderPosition -= 1.6 * m_width;
				}
				else if (m_sliderPosition < -0.8 * m_width)
				{
					m_sliderPosition += 1.6 * m_width;
				}
				
				switch (selectedNumber)
				{
				case 0:
					m_activeButton = eButton.ZERO;
					break;
				case 1:
					m_activeButton = eButton.ONE;
					break;
				case 2:
					m_activeButton = eButton.TWO;
					break;
				case 3:
					m_activeButton = eButton.THREE;
					break;
				case 4:
					m_activeButton = eButton.FOUR;
					break;
				case 5:
					m_activeButton = eButton.FIVE;
					break;
				case 6:
					m_activeButton = eButton.SIX;
					break;
				case 7:
					m_activeButton = eButton.SEVEN;
					break;
				case 8:
					m_activeButton = eButton.EIGHT;
					break;
				case 9:
					m_activeButton = eButton.NINE;
					break;
				}
			}
			
			// confirm action and clear active button
			if (m_activeButton != eButton.NONE && m_listener != null)
			{
				m_listener.onClick(this, m_activeButton);
			}
			m_activeButton = eButton.NONE;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			// find out where the move was
			double x = (event.getX () - m_offsetWidth) / m_width;
			double y = event.getY ();
			
			if (x < 0.1 || x > 0.9)
			{
				// out of horizontal region
				m_activeButton = eButton.NONE;
			}
			
			// slider region
			if (y >= m_numericSliderTop && y <= m_numericSliderTop + m_numericSliderHeight)
			{
				// adjust slider position and keep it between [-0.8, 0.8]
				m_sliderPosition = m_sliderPosition + 2.5f * (event.getX () - m_sliderPreviousX);
				if (m_sliderPosition > 0.8 * m_width)
				{
					m_sliderPosition -= 1.6 * m_width;
				}
				else if (m_sliderPosition < -0.8 * m_width)
				{
					m_sliderPosition += 1.6 * m_width;
				}
				m_sliderPreviousX = event.getX ();
				
				int selectedNumber = (int) ((m_numberSize / 2 - m_sliderPosition) / (m_numberSize) + 10) % 10;
				switch (selectedNumber)
				{
				case 0:
					m_activeButton = eButton.ZERO;
					break;
				case 1:
					m_activeButton = eButton.ONE;
					break;
				case 2:
					m_activeButton = eButton.TWO;
					break;
				case 3:
					m_activeButton = eButton.THREE;
					break;
				case 4:
					m_activeButton = eButton.FOUR;
					break;
				case 5:
					m_activeButton = eButton.FIVE;
					break;
				case 6:
					m_activeButton = eButton.SIX;
					break;
				case 7:
					m_activeButton = eButton.SEVEN;
					break;
				case 8:
					m_activeButton = eButton.EIGHT;
					break;
				case 9:
					m_activeButton = eButton.NINE;
					break;
				}
			}
			else if (y >= m_controlButtonsTop && y <= m_controlButtonsTop + m_controlButtonsHeight)
			{
				// navigate and control buttons
				y = (y - m_controlButtonsTop) * 0.75 / m_controlButtonsHeight;
				
				if (y < 0.15)
				{
					// top row
					if (x < 0.35 && m_activeButton != eButton.MUTE)
					{
						// mute
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.35 && x < 0.65 && m_activeButton != eButton.UP)
					{
						// up
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.65 && m_activeButton != eButton.INFO)
					{
						// info
						m_activeButton = eButton.NONE;
					}
				}
				else if (y < 0.35)
				{
					// middle row
					if (x < 0.35 && m_activeButton != eButton.LEFT)
					{
						// left
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.35 && x < 0.65 && m_activeButton != eButton.OK)
					{
						// ok
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.65 && m_activeButton != eButton.RIGHT)
					{
						// right
						m_activeButton = eButton.NONE;
					}
				}
				else if (y < 0.5)
				{
					// bottom row
					if (x < 0.35 && m_activeButton != eButton.REWIND)
					{
						// rewind
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.35 && x < 0.65 && m_activeButton != eButton.DOWN)
					{
						// down
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.65 && m_activeButton != eButton.FORWARD)
					{
						// forward
						m_activeButton = eButton.NONE;
					}
				}
				else if (y >= 0.55 && y <= 0.75)
				{
					// control buttons
					if (x < 0.25 && m_activeButton != eButton.PREVIOUS)
					{
						// previous
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.25 && x < 0.4 && m_activeButton != eButton.STOP)
					{
						// stop
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.4 && x < 0.6 && m_activeButton != eButton.PLAYPAUSE)
					{
						// play/pause
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.6 && x < 0.75 && m_activeButton != eButton.RECORD)
					{
						// record
						m_activeButton = eButton.NONE;
					}
					else if (x >= 0.75 && m_activeButton != eButton.NEXT)
					{
						// next
						m_activeButton = eButton.NONE;
					}
				}
				else
				{
					m_activeButton = eButton.NONE;
				}
			}
			else if (y >= m_menuButtonsTop && y <= m_menuButtonsTop + m_menuButtonsHeight)
			{
				// application buttons
				if (x < 0.2 || x > 0.8)
				{
					// nothing selected
					m_activeButton = eButton.NONE;
				}
				
				if (x < 0.4 && m_activeButton != eButton.HOME)
				{
					// home
					m_activeButton = eButton.NONE;
				}
				else if (x >= 0.4 && x < 0.6 && m_activeButton != eButton.MENU)
				{
					// menu
					m_activeButton = eButton.NONE;
				}
				else if (x >= 0.6 && m_activeButton != eButton.BACK)
				{
					// back
					m_activeButton = eButton.NONE;
				}
			}
			else if (y >= m_colorButtonsTop && y <= m_colorButtonsTop + m_colorButtonsHeight)
			{
				// color buttons
				if (x < 0.3 && m_activeButton != eButton.RED)
				{
					// red
					m_activeButton = eButton.NONE;
				}
				else if (x >= 0.3 && x < 0.5 && m_activeButton != eButton.GREEN)
				{
					// green
					m_activeButton = eButton.NONE;
				}
				else if (x >= 0.5 && x < 0.7 && m_activeButton != eButton.YELLOW)
				{
					// yellow
					m_activeButton = eButton.NONE;
				}
				else if (x >= 0.7 && m_activeButton != eButton.BLUE)
				{
					// blue
					m_activeButton = eButton.NONE;
				}
			}
			else
			{
				m_activeButton = eButton.NONE;
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			// find out where the action was
			double x = (event.getX () - m_offsetWidth) / m_width;
			double y = event.getY ();
				
			if (y >= m_numericSliderTop && y <= m_numericSliderTop + m_numericSliderHeight)
			{
				// slider region
				m_sliderPreviousX = event.getX ();
			}
			else if (y >= m_controlButtonsTop && y <= m_controlButtonsTop + m_controlButtonsHeight)
			{
				// navigate and control buttons
				y = (y - m_controlButtonsTop) * 0.75 / m_controlButtonsHeight;
				
				if (y < 0.15)
				{
					// top row
					if (x < 0.35)
					{
						// mute
						m_activeButton = eButton.MUTE;
					}
					else if (x < 0.65)
					{
						// up
						m_activeButton = eButton.UP;
					}
					else
					{
						// info
						m_activeButton = eButton.INFO;
					}
				}
				else if (y < 0.35)
				{
					// middle row
					if (x < 0.35)
					{
						// left
						m_activeButton = eButton.LEFT;
					}
					else if (x < 0.65)
					{
						// ok
						m_activeButton = eButton.OK;
					}
					else
					{
						// right
						m_activeButton = eButton.RIGHT;
					}
				}
				else if (y < 0.5)
				{
					// bottom row
					if (x < 0.35)
					{
						// rewind
						m_activeButton = eButton.REWIND;
					}
					else if (x < 0.65)
					{
						// down
						m_activeButton = eButton.DOWN;
					}
					else
					{
						// forward
						m_activeButton = eButton.FORWARD;
					}
				}
				else if (y >= 0.55 && y <= 0.75)
				{
					// control buttons
					if (x < 0.25)
					{
						// previous
						m_activeButton = eButton.PREVIOUS;
					}
					else if (x < 0.4)
					{
						// stop
						m_activeButton = eButton.STOP;
					}
					else if (x < 0.6)
					{
						// play/pause
						m_activeButton = eButton.PLAYPAUSE;
					}
					else if (x < 0.75)
					{
						// record
						m_activeButton = eButton.RECORD;
					}
					else
					{
						// next
						m_activeButton = eButton.NEXT;
					}
				}
				else
				{
					// nothing selected
					m_activeButton = eButton.NONE;
				}
			}
			else if (y >= m_menuButtonsTop && y <= m_menuButtonsTop + m_menuButtonsHeight)
			{
				// application buttons
				if (x < 0.2 || x > 0.8)
				{
					// nothing selected
					m_activeButton = eButton.NONE;
				}
				
				if (x < 0.4)
				{
					// home
					m_activeButton = eButton.HOME;
				}
				else if (x < 0.6)
				{
					// menu
					m_activeButton = eButton.MENU;
				}
				else
				{
					// back
					m_activeButton = eButton.BACK;
				}
			}
			else if (y >= m_colorButtonsTop && y <= m_colorButtonsTop + m_colorButtonsHeight)
			{
				// color buttons
				if (x < 0.3)
				{
					// red
					m_activeButton = eButton.RED;
				}
				else if (x < 0.5)
				{
					// green
					m_activeButton = eButton.GREEN;
				}
				else if (x < 0.7)
				{
					// yellow
					m_activeButton = eButton.YELLOW;
				}
				else
				{
					// blue
					m_activeButton = eButton.BLUE;
				}
			}
			else
			{
				m_activeButton = eButton.NONE;
			}
		}
		
		invalidate ();
		return true;
	}
}
