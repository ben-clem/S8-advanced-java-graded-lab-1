package primes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Computes prime numbers efficiently by leveraging all the available CPUs. The
 * main method of this class is {@link #computePrimes}.
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
     * @return an {@code Iterable} object over the list of primes;
     * {@code Iterator.next()} returns the primes in ascending order.
     */
    public static Iterable<Long> computePrimes(long max) {

        List<Long> primes = new ArrayList<>();

        /* Sequential as in the reference method: 7/12
        for (long candidate = 1; candidate < max; candidate += 1) {
          if (PrimeComputerTester.isPrime(candidate)) {
            primes.add(candidate);
          }
        }*/

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        //splitting the load
        long chunkSize = (long) Math.ceil((double) max / availableProcessors); //each chunk size (rounded up)

        List<Long> numbers = LongStream.range(1, max - 1)
                .boxed()
                .collect(Collectors.toList());

        final AtomicInteger counter = new AtomicInteger();
        final Collection<List<Long>> chunks = numbers.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values();


        ExecutorService pool = Executors.newFixedThreadPool(availableProcessors);


        for (int i = 0; i < availableProcessors; i += 1) {
            Future<List<Long>> result = pool.submit(new MyChunkProcessor((List<Long>) chunks.toArray()[i]));
        }


        pool.shutdown();
        try {
            pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return primes;
    }

    public class MyChunkProcessor implements Callable<List<Long>> {

        private List<Long> chunk;

        public MyChunkProcessor(List<Long> chunk) {
            this.chunk = chunk;
        }

        public List<Long> call() {
            for (long candidate = 1; candidate < max; candidate += 1) {
                if (PrimeComputerTester.isPrime(candidate)) {
                    primes.add(candidate);
                }
            }

            return XX;
        }
    }
}
