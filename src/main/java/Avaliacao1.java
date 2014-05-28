
import utfpr.ct.dainf.if62c.avaliacao.Poligonal;
import utfpr.ct.dainf.if62c.avaliacao.Ponto;

/**
 *
 * @author Wilson
 */
public class Avaliacao1 {
    public static void main(String[] args) {
        Ponto[] v = {
            new Ponto(1, 1),
            new Ponto(9, 1),
            new Ponto(6, 3),
            new Ponto(3, 3),
            new Ponto(1, 1)
        };
        Poligonal poli = new Poligonal(v);
        System.out.println("Área da poligonal = " + poli.getArea());
        System.out.println("Perímetro da poligonal = " + poli.getPerimetro());
    }
}
