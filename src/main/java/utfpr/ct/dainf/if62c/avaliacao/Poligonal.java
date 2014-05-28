package utfpr.ct.dainf.if62c.avaliacao;

/**
 * Representa uma poligonal fechada.
 * @author Nome do Aluno <email@do.aluno>
 */
public class Poligonal {
    private final Ponto[] vertices;
    
    /**
     * Construtor que inicializa a poligonal com um vetor de vértices recebido.
     * @param vertices Os vértices da poligonal
     */
    public Poligonal(Ponto[] vertices) {
        this.vertices = vertices;
    }
    
    /**
     * Construtor que inicializa um vetor de n+1 vértices.
     * @param n O número de vértices da poligonal
     */
    public Poligonal(int n) {
        // inicializa o vetor de vertices com n+1 vértices
        vertices = new Ponto[n+1];
    }
    
    public Ponto getVertice(int i) {
        return i < 0 || i >= vertices.length ? null : vertices[i];
    }
    
    public void setVertice(int i, Ponto vertice) {
        if (i >= 0 && i < vertices.length)
            vertices[i] = vertice;
    }
    
    public void setVertice(int i, double x, double y) {
        if (i >= 0 && i < vertices.length)
            vertices[i] = new Ponto(x, y);
    }
    
    public double getArea() {
        double area = 0;
        for (int i = 1; i < vertices.length; i++) {
            area += (vertices[i].getX() - vertices[i-1].getX()) *
                    (vertices[i].getY() + vertices[i-1].getY()); 
        }
        return Math.abs(area) / 2;
    }
    
    public double getPerimetro() {
        double perimetro = 0;
        for (int i = 1; i < vertices.length; i++) {
            perimetro += Math.sqrt(Math.pow((vertices[i].getX() - vertices[i-1].getX()), 2) +
                    Math.pow((vertices[i].getY() - vertices[i-1].getY()), 2)); 
        }
        return perimetro;
    }
    
    // Implmentar os seguintes métodos:
    // getVertice(int i)
    // setVertice(int i, double x, double y)
    // setVertice(int i, Ponto vertice)
    // getArea()
    // getPerimetro()

}
