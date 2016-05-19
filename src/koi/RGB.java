package koi;

public class RGB {
	public static final RGB red = new RGB(1,0,0);
	public static final RGB green = new RGB(0,1,0);
	public static final RGB blue = new RGB(0,0,1);
	public static final RGB black = new RGB(0.0, 0.0, 0.0);
	public static final RGB white = new RGB(1.0, 1.0, 1.0);
	
	double r;
	double g;
	double b;
	
	public RGB(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public RGB() {
		this.r = 0.0;
		this.g = 0.0;
		this.b = 0.0;
		
	}

	public RGB(double d) {
		this(d, d, d);
	}

	public RGB(RGB r) {
		this(r.r, r.g, r.b);
	}

	public int getRGB()
	{
		int _r = (int)(Koi.clamp(r, 0.0, 1.0) * 255);
		int _g = (int)(Koi.clamp(g, 0.0, 1.0) * 255);
		int _b = (int)(Koi.clamp(b, 0.0, 1.0) * 255);

		return (_r << 16) | (_g << 8) | _b;
	}
	
	public int[] getComponents()
	{
		int _r = (int)(Koi.clamp(r, 0.0, 1.0) * 255);
		int _g = (int)(Koi.clamp(g, 0.0, 1.0) * 255);
		int _b = (int)(Koi.clamp(b, 0.0, 1.0) * 255);
		int res[]= {_r, _g, _b};
		return res;
	}
	
	public RGB plus(RGB rgb)
	{
		RGB result = new RGB(); 
		result.r = r + rgb.r;
		result.g = g + rgb.g;
		result.b = b + rgb.b;
		return result;
	}
	
	public RGB minus(RGB rgb)
	{
		RGB result = new RGB(); 
		result.r = r - rgb.r;
		result.g = g - rgb.g;
		result.b = b - rgb.b;
		return result;
	}
	
	public RGB plusEquals(RGB rgb)
	{
		 r += rgb.r;
		 g += rgb.g;
		 b += rgb.b;
		 return this;
	}
	
	public RGB minusEquals(RGB rgb)
	{
		 r -= rgb.r;
		 g -= rgb.g;
		 b -= rgb.b;
		 return this;
	}
	
	public RGB times(RGB rgb)
	{
		RGB result = new RGB(); 
		result.r = r * rgb.r;
		result.g = g * rgb.g;
		result.b = b * rgb.b;
		return result;
	}
	
	public RGB timesEquals(RGB rgb)
	{
		r *= rgb.r;
		g *= rgb.g;
		b *= rgb.b;
		return this;
	}
	
	public RGB divide(RGB rgb)
	{
		RGB result = new RGB(); 
		result.r = r / rgb.r;
		result.g = g / rgb.g;
		result.b = b / rgb.b;
		return result;
	}
	
	public RGB divideEquals(RGB rgb)
	{
		r /= rgb.r;
		g /= rgb.g;
		b /= rgb.b;
		return this;
	}
	
	public RGB divideEquals(double s)
	{
		r /= s;
		g /= s;
		b /= s;
		return this;
	}
	
	
	public RGB pow(double s)
	{
		double _r = Math.pow(r, s);
		double _g = Math.pow(g, s);
		double _b = Math.pow(b, s);
		RGB result = new RGB(_r, _g, _b); 
		return result;
	}
	
	public RGB negate()
	{
		RGB result = new RGB(-r, -g, -b); 
		return result;
	}
	
	public RGB clamp()
	{
		double _r = (Koi.clamp(r, 0.0, 1.0));
		double _g = (Koi.clamp(g, 0.0, 1.0));
		double _b = (Koi.clamp(b, 0.0, 1.0));
		return new RGB(_r, _g, _b); 
	}
	
	public double average()
	{
		return (r + g + b) / 3.0;
	}

	public double getRed() {
		return r;
	}

	public void setRed(double r) {
		this.r = r;
	}

	public double getGreen() {
		return g;
	}

	public void setGreen(double g) {
		this.g = g;
	}

	public double getBlue() {
		return b;
	}

	public void setBlue(double b) {
		this.b = b;
	}

	public RGB abs() {
		return new RGB(Math.abs(r), Math.abs(g), Math.abs(b));
	}

	public RGB divide(double s) {
		s = 1.0 / s;
		return new RGB(r * s, g * s, b * s);
	}

	public RGB times(double s) {
		return new RGB(r * s, g * s, b * s);
	}

	public RGB timesEquals(double s) {
		r *= s;
		g *= s;
		b *= s;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		RGB rgb = (RGB)obj;
		return Double.compare(r, rgb.r) == 0 && Double.compare(g, rgb.g) == 0 && Double.compare(b, rgb.b) == 0;
	}
}
