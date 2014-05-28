package utfpr.ct.dainf.if62c.avaliacao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.testng.Assert.*;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 *
 * @author Wilson
 */
public class Avaliacao1Test {

    private static final int MAX_POINTS = 200;
    private int points = 0;
    private Class mainClass;
    private Method mainMethod;
    
    public Avaliacao1Test() {
    }

    
    @BeforeSuite
    public void beforeSuite() {
        
    }
    
    @AfterSuite
    public void afterSuite() {
        Reporter.log(String.format("RESULTADO FINAL: %d de %d pontos. Nota = %4.1f",
            points, MAX_POINTS, points / 100.0), true);
    }
    
    private URL findDefaultPackageURL() {
        URL url = null;
        try {
            Enumeration<URL> urls = ClassLoader.getSystemResources("");
            while (urls.hasMoreElements()) {
                url = urls.nextElement();
                if (!url.toString().endsWith("/test-classes/")) {
                    break;
                }
            }
        } catch (Exception e) {
            
        }
        return url;
    }
    
    private URI findDefaultPackageURI() {
        URI uri;
        try {
            uri = findDefaultPackageURL().toURI();
        } catch (URISyntaxException e) {
            uri = null;
        }
        return uri;
    }

    private File findFileInSubdir(File dir, String fileName) {
        File[] files = dir.listFiles();
        File found = null;
        int i = 0;
        
        while (i < files.length && found == null) {
            if (files[i].isDirectory())
                return findFileInSubdir(files[i], fileName);
            else
                if (files[i].getName().equals(fileName))
                    found = files[i];
            i++;
        }
        return found;
    }
    
    private File findFile(String fileName) {
        File file = new File(findDefaultPackageURI());
        return findFileInSubdir(file, fileName);
    }
    
    private String findQualifiedClassName(String className) {
        String pkg = null;
        File defpkg = new File(findDefaultPackageURI());
        File classFile = findFile(className + ".class");
        if (classFile != null) {
            StringBuilder sb = new StringBuilder(classFile.toString());
            sb.delete(0, defpkg.toString().length()+1);
            sb.delete(sb.length()-6, sb.length());
            int idx = 0;
            while ((idx = sb.indexOf(File.separator, idx)) >= 0) {
                sb.replace(idx, idx+1, ".");
            }
            pkg = sb.toString();
        }
        return pkg;
    }
      
    @Test
    public void mainClassTest() {
        Class c = null;
        String avaliacao1Qcn = findQualifiedClassName("Avaliacao1");
        try {
            if (avaliacao1Qcn != null)
                mainClass = Class.forName(avaliacao1Qcn);
        } catch (ClassNotFoundException e) {
            Reporter.log(e.getLocalizedMessage(), true);
        }
        assertNotNull(mainClass, "Classe 'Avaliacao1' não encontrada.");
        if (mainClass.getPackage() != null)
            Reporter.log("Classe 'Avaliacao1' encontrada no pacote incorreto: " + mainClass.getPackage().getName(), true);
        else {
            Reporter.log("Classe 'Avaliacao1' encontrada: +5 pontos", true);
            points += 5;
        }
    }
    
    @Test(dependsOnMethods = {"mainClassTest"})
    public void mainMethodTest() {
        try {
            mainMethod = mainClass.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            
        }
        assertNotNull(mainMethod, "Método 'main' não encontrado ou com argumentos incorretos.");
        assertEquals(mainMethod.getReturnType(), void.class,
            "Método 'main' não retorna o tipo correto.");
        int mod = mainMethod.getModifiers();
        assertEquals(mod, Modifier.PUBLIC | Modifier.STATIC,
            "Método 'main' encontrado mas com modificadores incorretos.");
        Reporter.log("Método 'main' declarado corretamente: +5 pontos", true);
        points += 5;
    }
    
    
    @Test
    public void pontoTest() {
        Class ponto = null;
        Constructor c = null;
        Object obj = null;
        double xTest = Math.random(), yTest = Math.random();
        double x = Math.random(), y = Math.random();
        try {
            ponto = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            c = ponto.getConstructor(double.class, double.class);
            obj = c.newInstance(xTest, yTest);
            Field fx = ponto.getDeclaredField("x");
            fx.setAccessible(true);
            x = fx.getDouble(obj);
            Field fy = ponto.getDeclaredField("y");
            fy.setAccessible(true);
            y = fy.getDouble(obj);
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(ponto, "Classe 'utfpr.ct.dainf.if62c.avaliacao.Ponto' não encontrada.");
        assertNotNull(c, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Ponto(double, double)' não encontrado.");
        assertNotNull(obj, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Ponto(double, double)' não pode ser invocado.");
        assertEquals(x, xTest, "Construtor 'Ponto(double, double)' não atribui o valor de x");
        Reporter.log("Construtor 'Ponto(double, double)' atribui corretamente o valor de x: +5 pontos", true);
        points += 5;
        assertEquals(y, yTest, "Construtor 'Ponto(double, double)' não atribui o valor de y");
        Reporter.log("Construtor 'Ponto(double, double)' atribui corretamente o valor de y: +5 pontos", true);
        points += 5;
    }
    
    @Test(dependsOnMethods = {"pontoTest"})
    public void getXTest() {
        Class ponto;
        Constructor c;
        Object obj;
        double xTest = Math.random(), yTest = Math.random();
        double x = Math.random(), y = Math.random();
        Method getX = null;
        try {
            ponto = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            c = ponto.getConstructor(double.class, double.class);
            obj = c.newInstance(xTest, yTest);
            getX = ponto.getMethod("getX");
            x = (double) getX.invoke(obj);
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(getX, "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getX()' não encontrado.");
        assertEquals(getX.getReturnType(), double.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getX()' não retorna o tipo correto.");
        assertEquals(x, xTest, "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getX()' não retorna o valor correto de x.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getX()' correto: +5 pontos", true);
        points += 5;
    }

    @Test(dependsOnMethods = {"pontoTest"})
    public void getYTest() {
        Class ponto;
        Constructor c;
        Object obj;
        double xTest = Math.random(), yTest = Math.random();
        double x = Math.random(), y = Math.random();
        Method getY = null;
        try {
            ponto = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            c = ponto.getConstructor(double.class, double.class);
            obj = c.newInstance(xTest, yTest);
            getY = ponto.getMethod("getY");
            y = (double) getY.invoke(obj);
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(getY, "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getY()' não encontrado.");
        assertEquals(getY.getReturnType(), double.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getY()' não retorna o tipo correto.");
        assertEquals(y, yTest, "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getY()' não retorna o valor correto de y.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.getY()' correto: +5 pontos", true);
        points += 5;
    }

    @Test(dependsOnMethods = {"pontoTest"})
    public void setXYTest() {
        Class ponto;
        Constructor c;
        Object obj;
        double xTest = Math.random(), yTest = Math.random();
        double x = Math.random(), y = Math.random();
        Method setXY = null;
        try {
            ponto = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            c = ponto.getConstructor(double.class, double.class);
            obj = c.newInstance(Math.random(), Math.random());
            setXY = ponto.getMethod("setXY", double.class, double.class);
            setXY.invoke(obj, xTest, yTest);
            Field fx = ponto.getDeclaredField("x");
            fx.setAccessible(true);
            x = fx.getDouble(obj);
            Field fy = ponto.getDeclaredField("y");
            fy.setAccessible(true);
            y = fy.getDouble(obj);
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(setXY, "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' não encontrado.");
        assertEquals(setXY.getReturnType(), void.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' não retorna o tipo correto.");
        if (x == xTest) {
            Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' atribui x corretamente: +5 pontos", true);
            points += 5;
        } else {
            Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' não atribui x corretamente.", true);
        }
        if (y == yTest) {
            Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' atribui y corretamente: +5 pontos", true);
            points += 5;
        } else {
            Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Ponto.setXY(double, double)' não atribui y corretamente.", true);
        }
    }
    
    @Test(dependsOnMethods = {"pontoTest"})
    public void toStringTest() {
        Class ponto;
        Constructor c;
        Object obj;
        double xTest = Math.random(), yTest = Math.random();
        double x = Math.random(), y = Math.random();
        Method toStr = null;
        String str = null;
        try {
            ponto = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            c = ponto.getConstructor(double.class, double.class);
            obj = c.newInstance(xTest, yTest);
            toStr = ponto.getDeclaredMethod("toString");
            str = (String) toStr.invoke(obj);
        } catch (InvocationTargetException e) {
            Reporter.log("Método toString() não pode ser executado.", true);
        } catch (Exception e) {
            Reporter.log(e.getMessage(), true);
        }
        assertNotNull(toStr, "Método toString() não encontrado");
        assertEquals(toStr.getReturnType(), String.class, "Método toString não retorna o tipo correto.");
        Pattern pattern = Pattern.compile("^[ ]*\\([ ]*(-*\\d+[,.]\\d+)[ ]*,[ ]*(-*\\d+[,.]\\d+)[ ]*\\)[ ]*$");
        Matcher matcher = pattern.matcher(str);
        assertTrue(matcher.matches(), "toString() não retorna o formato correto. Retornou " + str);
        if (Double.parseDouble(matcher.group(1)) == xTest) {
            Reporter.log("Valor de x corresponde ao retorno de toString(): +10 pontos", true);
            points += 10;
        } else {
            Reporter.log("Valor de x não corresponde ao retorno de toString().", true);
        }
        if (Double.parseDouble(matcher.group(2)) == yTest) {
            Reporter.log("Valor de y corresponde ao retorno de toString(): +10 pontos", true);
            points += 10;
        } else {
            Reporter.log("Valor de y não corresponde ao retorno de toString().", true);
        }
    }
    
    @Test
    public void poligonalTest() {
        Class clazz = null;
        Constructor c = null;
        Object obj = null;
        Object[] v = null;
        int nTest = (int) (Math.random() * 10 + 1);
        int len = 0;
        try {
            clazz = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            c = clazz.getConstructor(int.class);
            obj = c.newInstance(nTest);
            Field f = clazz.getDeclaredField("vertices");
            f.setAccessible(true);
            v = (Object[])f.get(obj);
            if (v != null)
                len = v.length;
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(clazz, "Classe 'utfpr.ct.dainf.if62c.avaliacao.Poligonal' não encontrada.");
        assertNotNull(c, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' não encontrado.");
        assertNotNull(obj, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' não pode ser invocado.");
        assertNotNull(v, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' não inicializou vértices.");
        Reporter.log("Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' inicializou vertices: +10 pontos", true);
        points += 10;
        assertEquals(len, nTest+1, "Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' não cria o vetor com tamanho correto");
        Reporter.log("Construtor 'utfpr.ct.dainf.if62c.avaliacao.Poligonal(int)' cria vetor corretamente: +10 pontos", true);
        points += 10;
    }
    
    @Test(dependsOnMethods = {"poligonalTest", "pontoTest"})
    public void getVerticeTest() {
        Class polic;
        Class pontoc;
        Constructor c;
        Object obj;
        int n = (int)(Math.random() * 10);
        int i = n/2;
        Object ret = null;
        Method getVertice = null;
        double x = Math.random(), y = Math.random();
        double xTest = Math.random(), yTest = Math.random();
        boolean checkIndex = false;
        try {
            polic = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            obj = polic.getConstructor(int.class).newInstance(n);
            pontoc = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            Field f = polic.getDeclaredField("vertices");
            f.setAccessible(true);
            Object[] v = (Object[])f.get(obj);
            if (v != null) {
                v[i] = pontoc.getConstructor(double.class, double.class).newInstance(xTest, yTest);
                getVertice = polic.getMethod("getVertice", int.class);
                if (getVertice != null) {
                    ret = getVertice.invoke(obj, i);
                    if (ret != null) {
                        Field fx = pontoc.getDeclaredField("x");
                        fx.setAccessible(true);
                        x = fx.getDouble(ret);
                        Field fy = pontoc.getDeclaredField("y");
                        fy.setAccessible(true);
                        y = fy.getDouble(ret);
                    }
                    try {
                        checkIndex = getVertice.invoke(obj, n+2) == null;
                        checkIndex = getVertice.invoke(obj, -1) == null;
                    } catch (IndexOutOfBoundsException e) {
                        checkIndex = false;
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(getVertice, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' não encontrado.");
        assertEquals(getVertice.getReturnType().getName(), "utfpr.ct.dainf.if62c.avaliacao.Ponto",  "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' não retorna o tipo correto.");
        assertNotNull(ret, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' não retorna um valor");
        assertEquals(x, xTest, "Valor atribuido e retornado por 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' não coincidem.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' retorna o ponto correto: +10 pontos");
        points += 10;
        assertTrue(checkIndex, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' não valida índice.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getVertice(int)' valida índice: +10 pontos", true);
        points += 10;
    }
    
    
    @Test(dependsOnMethods = {"poligonalTest", "pontoTest"})
    public void setVerticeV() {
        Class polic;
        Class pontoc;
        Constructor c;
        Object obj;
        int n = (int)(Math.random() * 10);
        int i = n/2;
        Method setVertice = null;
        double x = Math.random(), y = Math.random();
        double xTest = Math.random(), yTest = Math.random();
        boolean checkIndex = false;
        try {
            polic = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            obj = polic.getConstructor(int.class).newInstance(n);
            pontoc = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            Field f = polic.getDeclaredField("vertices");
            f.setAccessible(true);
            Object[] v = (Object[])f.get(obj);
            if (v != null) {
                Object p = pontoc.getConstructor(double.class, double.class).newInstance(xTest, yTest);
                setVertice = polic.getMethod("setVertice", int.class, p.getClass());
                if (setVertice != null) {
                    setVertice.invoke(obj, i, p);
                    Field fx = pontoc.getDeclaredField("x");
                    fx.setAccessible(true);
                    x = fx.getDouble(v[i]);
                    Field fy = pontoc.getDeclaredField("y");
                    fy.setAccessible(true);
                    y = fy.getDouble(v[i]);
                    try {
                        setVertice.invoke(obj, n+2, p);
                        setVertice.invoke(obj, -1, p);
                        checkIndex = true;
                    } catch (IndexOutOfBoundsException e) {
                        checkIndex = false;
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(setVertice, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' não encontrado.");
        assertEquals(setVertice.getReturnType(), void.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' não retorna o tipo correto.");
        assertEquals(x, xTest, "Valor x atribuido por 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' não coincide.");
        assertEquals(y, yTest, "Valor y atribuido por 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' não coincide.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' atribui o vértice corretamente: +10 pontos");
        points += 10;
        assertTrue(checkIndex, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' não valida índice.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, Ponto)' valida índice: +10 pontos", true);
        points += 10;
    }
    
    @Test(dependsOnMethods = {"poligonalTest", "pontoTest"})
    public void setVerticeXY() {
        Class polic;
        Class pontoc;
        Constructor c;
        Object obj;
        int n = (int)(Math.random() * 10);
        int i = n/2;
        Method setVertice = null;
        double x = Math.random(), y = Math.random();
        double xTest = Math.random(), yTest = Math.random();
        boolean checkIndex = false;
        try {
            polic = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            obj = polic.getConstructor(int.class).newInstance(n);
            pontoc = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            Field f = polic.getDeclaredField("vertices");
            f.setAccessible(true);
            Object[] v = (Object[])f.get(obj);
            if (v != null) {
                Object p = pontoc.getConstructor(double.class, double.class).newInstance(xTest, yTest);
                setVertice = polic.getMethod("setVertice", int.class, double.class, double.class);
                if (setVertice != null) {
                    setVertice.invoke(obj, i, xTest, yTest);
                    Field fx = pontoc.getDeclaredField("x");
                    fx.setAccessible(true);
                    x = fx.getDouble(v[i]);
                    Field fy = pontoc.getDeclaredField("y");
                    fy.setAccessible(true);
                    y = fy.getDouble(v[i]);
                    try {
                        setVertice.invoke(obj, n+2, xTest, yTest);
                        setVertice.invoke(obj, -1, xTest, yTest);
                        checkIndex = true;
                    } catch (IndexOutOfBoundsException e) {
                        checkIndex = false;
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(setVertice, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' não encontrado.");
        assertEquals(setVertice.getReturnType(), void.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' não retorna o tipo correto.");
        assertEquals(x, xTest, "Valor atribuido por 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' não coincide.");
        assertEquals(y, yTest, "Valor atribuido por 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' não coincide.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' atribui o vértice corretamente: +10 pontos");
        points += 10;
        assertTrue(checkIndex, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' não valida índice.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.setVertice(int, double, double)' valida índice: +10 pontos", true);
        points += 10;
    }
    
    @Test(dependsOnMethods = {"poligonalTest", "pontoTest"})
    public void getAreaTest() {
        Class polic;
        Class pontoc;
        Object obj;
        Method getArea = null;
        double aTest = Math.random(), yTest = 1 - (Math.random() + 1);
        double a = Math.random();
        try {
            polic = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            obj = polic.getConstructor(int.class).newInstance(3);
            pontoc = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            Field f = polic.getDeclaredField("vertices");
            f.setAccessible(true);
            Object[] v = (Object[])f.get(obj);
            if (v != null) {
                Constructor pc = pontoc.getConstructor(double.class, double.class);
                v[0] = pc.newInstance(1.0, 1.0);
                v[1] = pc.newInstance(1.0, yTest);
                v[2] = pc.newInstance(5.0, 1.0);
                v[3] = v[0];
                aTest = (4 * (yTest - 1)) / 2;
                a = aTest + Math.random() * 10;
                getArea = polic.getMethod("getArea");
                if (getArea != null) {
                    a = (double) getArea.invoke(obj);
                }
            }
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(getArea, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' não encontrado.");
        assertEquals(getArea.getReturnType(), double.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' não retorna o tipo correto.");
        assertEquals(Math.abs(a), Math.abs(aTest), "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' não retorna a área correta.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' calcula a área corretamente: +10 pontos", true);
        points += 10;
        assertNotEquals(a, aTest, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' não retorna o módulo da área.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getArea()' calcula o módulo da área corretamente: +10 pontos", true);
        points += 10;
    }
    
    @Test(dependsOnMethods = {"poligonalTest", "pontoTest"})
    public void getPerimetroTest() {
        Class polic;
        Class pontoc;
        Object obj;
        Method getPerimetro = null;
        double pTest = Math.random(), yTest = 1 + (Math.random() + 1);
        double p = Math.random();
        try {
            polic = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Poligonal");
            obj = polic.getConstructor(int.class).newInstance(3);
            pontoc = Class.forName("utfpr.ct.dainf.if62c.avaliacao.Ponto");
            Field f = polic.getDeclaredField("vertices");
            f.setAccessible(true);
            Object[] v = (Object[])f.get(obj);
            if (v != null) {
                Constructor pc = pontoc.getConstructor(double.class, double.class);
                v[0] = pc.newInstance(1.0, 1.0);
                v[1] = pc.newInstance(1.0, yTest);
                v[2] = pc.newInstance(5.0, 1.0);
                v[3] = v[0];
                pTest = 4 + yTest - 1 + Math.sqrt(16 + Math.pow(yTest - 1, 2));
                p = pTest + Math.random() * 10;
                getPerimetro = polic.getMethod("getPerimetro");
                if (getPerimetro != null) {
                    p = (double) getPerimetro.invoke(obj);
                }
            }
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        } 
        
        assertNotNull(getPerimetro, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getPerimetro()' não encontrado.");
        assertEquals(getPerimetro.getReturnType(), double.class,  "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getPerimetro()' não retorna o tipo correto.");
        assertTrue(Math.abs(p - pTest) < 1e-5, "Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getPerimetro()' não retorna o perímetro correto.");
        Reporter.log("Método 'utfpr.ct.dainf.if62c.avaliacao.Poligonal.getPerimetro()' calcula o perímetro corretamente: +20 pontos", true);
        points += 20;
    }
    
    @Test(dependsOnMethods = { "mainClassTest", "mainMethodTest" })
    public void outputTest() {
        boolean mainExec = false;
        PrintStream out = System.out;
        PrintStream err = System.err;
        ByteArrayOutputStream outCapture = new ByteArrayOutputStream();
        ByteArrayOutputStream errCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outCapture, true));
        System.setErr(new PrintStream(errCapture, true));
        try {
            Object av1 = mainClass.getConstructor().newInstance();
            mainMethod.invoke(av1, (String)null);
            mainExec = true;
        } catch (Exception e) {
            Reporter.log(e.getLocalizedMessage(), true);
        }
        System.setOut(out);
        System.setErr(err);
        String outStr = outCapture.toString();
        String errStr = errCapture.toString();
        assertTrue(mainExec, "Método main não pode ser executado.");
        Reporter.log("Saída de execução do método main:\n" + outStr, true);
        Reporter.log("Saída de erro do método main:\n" + errStr, true);
        if (!outStr.trim().isEmpty()) {
            Reporter.log("Método main escreveu algo: +5 pontos", true);
            points += 5;
        }
        Pattern pattern = Pattern.compile("^\\D*\\d.*$", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(outStr);
        if (matcher.matches()) {
            Reporter.log("Método main escreveu algo numérico: +5 pontos", true);
            points += 5;
        }
        if (outStr.contains("11")) {
            Reporter.log("Método main escreveu a área: +5 pontos", true);
            points += 5;
        }
        if (outStr.contains("17.43") || outStr.contains("17,43")) {
            Reporter.log("Método main escreveu o perímetro: +5 pontos", true);
            points += 5;
        }
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
