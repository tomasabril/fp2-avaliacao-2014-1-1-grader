package utfpr.ct.dainf.if62c.avaliacao;

/**
 * Representa um ponto no plano.
 * @author Nome do Aluno <email@do.aluno>
 */
public class Ponto {
    
    // as coordenadas do ponto
    private double x;
    private double y;
    
    /**
     * Construtor que inicializa as coordenadas do ponto.
     * @param x A coordenada x do ponto
     * @param y A coordenada y do ponto
     */
    public Ponto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // Implementar os seguintes métodos:
    // getX()
    // getY()
    // setXY(double x, double y)
    
    /**
     * Retorna uma representação textual do ponto no formato (x, y).
     * @return 
     */
    @Override
    public String toString() {
        // TODO: retona um String com as coordenadas do ponto no formato (x, y),
        // por exemplo, (1,11, 2,22) para um ponto com coordenadas x = 1.11 e
        // y = 2.22
        return "(" + x + ", " + y + ")";
    }
    
}
