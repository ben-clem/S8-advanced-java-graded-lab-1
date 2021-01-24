package primes;

import java.util.Collections;

/**
 * Computes prime numbers efficiently by leveraging all the available CPUs. The
 * main method of this class is {@link computePrimes}.
 * <p>
 * <i>Note to implementors:</i> You can add to this class any field, method or
 * nested class you need to implement {@code computePrimes}.
 *
 * @author Jean-Michel Busca
 */
public class PrimeComputer {

  /**
   * Efficiently computes prime numbers up to the specified upper bound
   * (excluded).
   * <p>
   * <i>Note to implementors:</i>
   * <ul>
   * <li> <b>Do not alter</b> the interface of this method; if you do, the test
   * program will not work.
   * <li> Before returning, the method must release all the resources it
   * allocated, in particular threads - whether daemon or not.
   * <li> The method must use the {@link PrimeComputerTester#isPrime} method or
   * one of the two allowed variations (see the lab paper).
   * <li> The method is declared to return an {@code Iterable} object - which is
   * less restrictive than {@code List} or {@code Collection} - not to constrain
   * the implementation.
   * </ul>
   *
   * @param max the upper bound of primes
   *
   * @return an {@code Iterable} object over the list of primes;
   * {@code Iterator.next()} returns the primes in ascending order.
   */
  public static Iterable<Long> computePrimes(long max) {
    // TODO
    return Collections.emptyList();
  }

}
