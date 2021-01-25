package primes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
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

        int multiplier = 2;


        int availableProcessors = Runtime.getRuntime().availableProcessors();

        //splitting the load
        //long chunkSize = (long) Math.ceil((double) max / availableProcessors / multiplier); //each chunk size (rounded up)
        List<Integer> chunkIndexes = getIdx((int) max, availableProcessors * multiplier);


        List<Long> numbers = LongStream.range(1, max - 1)
                .boxed()
                .collect(Collectors.toList());


        Collection<List<Long>> chunks = new ArrayList<List<Long>>();

        int last = 0;
        for (int i = 1; i < chunkIndexes.size(); i++) {
            chunks.add(numbers.subList(chunkIndexes.get(i - 1), chunkIndexes.get(i)));


        }


        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<List<Long>>> list = new ArrayList<Future<List<Long>>>();

        for (int i = 0; i < availableProcessors * multiplier; i += 1) {

            Future<List<Long>> result = pool.submit(new MyChunkProcessor((List<Long>) chunks.toArray()[i], i));
            list.add(result);
        }

        for (Future<List<Long>> fut : list) {
            try {
                primes.addAll(fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
        return primes;
    }

    public static class MyChunkProcessor implements Callable<List<Long>> {
        List<Long> chunk;
        List<Long> primes;
        int i;

        public MyChunkProcessor(List<Long> chunk, int i) {
            this.chunk = chunk;
            this.primes = new ArrayList<>();
            this.i = i;
        }

        public List<Long> call() {
            long start = System.currentTimeMillis();
            chunk.forEach((candidate) -> {
                if (PrimeComputerTester.isPrime(candidate)) {
                    primes.add(candidate);
                }
            });

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;

            return primes;
        }
    }

    /**
     * Function to compute chunk indexes so that they all have the same time complexity
     * @param n max number
     * @param cores nb of threads
     * @return chunks
     */
    public static ArrayList<Integer> getIdx(int n, int cores) {
        long sum_per_chunk = sumArr(n);
        sum_per_chunk = sum_per_chunk / cores;

        Long sum = (long) 0;
        ArrayList<Integer> res = new ArrayList<>();
        res.add(0);
        for (int i = 0; i < n; i++) {
            sum += complexity(i);
            if (sum > sum_per_chunk) {
                res.add(i);
                sum = (long) 0;
            }
        }
        res.add(n - 2);
        return res;
    }

    public static long sumArr(int n) {
        long res = 0;
        for (int i = 0; i < n; i++) {
            res += complexity(i);
        }
        return res;
    }

    public static long complexity(int i) {
        // We assumed the time complexity of the trial division was close to n.
        return i;
    }

}

