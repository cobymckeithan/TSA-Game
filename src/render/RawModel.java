package render;

public class RawModel {

	// Stuff OpenGL needs
	public int vaoID;
	public int vertexNumber;
	
	// Set the properties that OpenGL needs
	public RawModel(int vaoID, int vertexNumber) {
		this.vaoID = vaoID;
		this.vertexNumber = vertexNumber;
	}
	
}
