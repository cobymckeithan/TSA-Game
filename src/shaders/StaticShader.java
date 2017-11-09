package shaders;

public class StaticShader extends ShaderProgram {
	
	// File paths of shaders
	private static final String VERTEX_FILE = "/shaders/v.shader";
	private static final String FRAGMENT_FILE = "/shaders/f.shader";;

	public StaticShader() {
		// Compile shaders
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		// Bind VBO 0 to position
		super.bindAttribute(0, "position");
	}

}
