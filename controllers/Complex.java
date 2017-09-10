package controllers;

/**
 * ****************************************************************************
 * Compilation: javac Complex.java Execution: java Complex
 *
 * Data type for complex numbers.
 *
 * The data type is "immutable" so once you create and initialize a Complex
 * object, you cannot change it. The "final" keyword when declaring re and im
 * enforces this rule, making it a compile-time error to change the .re or .im
 * instance variables after they've been initialized.
 *
 * % java Complex a = 5.0 + 6.0i b = -3.0 + 4.0i Re(a) = 5.0 Im(a) = 6.0 b + a =
 * 2.0 + 10.0i a - b = 8.0 + 2.0i a * b = -39.0 + 2.0i b * a = -39.0 + 2.0i a /
 * b = 0.36 - 1.52i (a / b) * b = 5.0 + 6.0i conj(a) = 5.0 - 6.0i |a| =
 * 7.810249675906654 tan(a) = -6.685231390246571E-6 + 1.0000103108981198i
 *
 *****************************************************************************
 */
import java.util.Objects;

public class Complex {

    private final double re;   // la parte real
    private final double im;   // la parte imaginaria

    // crear un nuevo objeto dadas las parte real y la imaginaria
    /**
     * Constructor de la clase Complex
     *
     * @param real Parte Real
     * @param imag Parte Imaginaria
     */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    /**
     * Constructor de la clase Complex a partir de parámetros polares
     *
     * @param mag Magnitud
     * @param phase Fase
     * @param isRadian Indica si la fase está expresada en radianes
     */
    public Complex(double mag, double phase, boolean isRadian) {
        if (isRadian) {
            re = mag * Math.cos(phase);
            im = phase * Math.sin(phase);
        } else {
            re = mag * Math.cos(Math.toRadians(phase));
            im = phase * Math.sin(Math.toRadians(phase));
        }

    }

    // regresa una representación en String del objeto Complex invocador
    /**
     * Representación textual del objeto Complex
     *
     * @return real + imaginario i
     */
    public String toString() {
        if (im == 0) {
            return re + "";
        }
        if (re == 0) {
            return im + "i";
        }
        if (im < 0) {
            return re + " - " + (-im) + "i";
        }
        return re + " + " + im + "i";
    }

    // regresa magnitud
    /**
     * Devuelve la magnitud del objeto Complex
     *
     * @return Magnitud del objeto Complex
     */
    public double abs() {
        return Math.hypot(re, im);
    }

    // regresa ángulo, normalizado para estar entre -pi y pi
    /**
     * Devuelve el ángulo normalizado entre -pi y pi del objeto Complex
     *
     * @return Ángulo normalizado entre -pi y pi del objeto Complex
     */
    public double phase() {
        return Math.atan2(im, re);
    }

    // regresa un nuevo objeto Complex, cuyo valor es (this + b)
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (this + b)
     *
     * @param b Objeto Complex a sumar
     * @return this + b
     */
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // regresa un nuevo objeto Complex, cuyo valor es (this - b)
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (this - b)
     *
     * @param b Objeto Complex a restar
     * @return this - b
     */
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // regresa un nuevo objeto Complex, cuyo valor es (this * b)
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (this * b)
     *
     * @param b Objeto Complex a multiplicar
     * @return this * b
     */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // regresa un nuevo objeto Complex, cuyo valor es (this * alpha)
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (alpha * re, alpha * im)
     *
     * @param alpha Factor de escalamiento
     * @return alpha * re, alpha * im
     */
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // regresa un nuevo objeto Complex, cuyo valor es el conjugado de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es el conjugado de this
     *
     * @return Conjugado de Complex
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    // regresa un nuevo objeto Complex, cuyo valor es el recíproco de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es el recíproco de this
     *
     * @return Recíproco de Complex
     */
    public Complex reciprocal() {
        double scale = re * re + im * im;
        return new Complex(re / scale, -im / scale);
    }

    // regresa la parte real o imaginaria
    /**
     * Regresa la parte real de Complex
     *
     * @return re
     */
    public double re() {
        return re;
    }

    /**
     * Regresa la parte imaginaria de Complex
     *
     * @return im
     */
    public double im() {
        return im;
    }

    // regresa a/b
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (this / b)
     *
     * @param b Objeto Complex a dividir
     * @return this / b
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // regresa un nuevo objeto Complex, cuyo valor es el exponencial complejo de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es el exponencial complejo de
     * this
     *
     * @return Exponencial complejo de Complex
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // regresa un nuevo objeto Complex, cuyo valor es el seno complejo de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es el seno complejo de this
     *
     * @return Seno complejo de this
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // regresa un nuevo objeto Complex, cuyo valor es el coseno complejo de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es el coseno complejo de this
     *
     * @return Coseno complejo de this
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // regresa un nuevo objeto Complex, cuyo valor es la tangente compleja de this
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es la tangente compleja de
     * this
     *
     * @return Tangente compleja de this
     */
    public Complex tan() {
        return sin().divides(cos());
    }

    //una versión estática de suma
    /**
     * Regresa un nuevo objeto Complex, cuyo valor es (this + b) (Versión
     * estática)
     *@param a Objeto Complex a sumar
     * @param b Objeto Complex a sumar
     * @return this + b
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    // regresa true si Objeto x es igual a this, de lo contrario, regresa false
    /**
     * Regresa true si Objeto x es igual a this, de lo contrario, regresa false
     *
     * @param x Objeto de la clase Complex
     * @return true si x es igual a this, de lo contrario, regresa false
     */
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (this.getClass() != x.getClass()) {
            return false;
        }
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    // Código Hash del objeto Complex
    public int hashCode() {
        return Objects.hash(re, im);
    }

}
