package io.github.bluething.java.heapdump.klassified;

import io.github.bluething.java.heapdump.klassified.api.Posting;
import io.github.bluething.java.heapdump.klassified.jdbi.Advert;
import io.github.bluething.java.heapdump.klassified.jdbi.Advertiser;
import io.github.bluething.java.heapdump.klassified.resources.PostingCache;

import java.io.IOException;
import java.util.Random;

public class MemoryLeakRunner
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        final Advertiser advertiser = new Advertiser("Keen Kustomer", "123 Fake Street");

        final Advert advert = new Advert(
            "Job",
            "Java Developer",
            "Agile, TDD, Spring, Hibernate ... ",
            advertiser);

        final Random random = new Random();
        final PostingCache cache = new PostingCache();

        Thread.sleep(5000);
        System.out.println("Starting");

        while (true)
        {
            final Posting posting = cache.get(advert);
            if (random.nextDouble() < 0.0000001)
            {
                System.out.println(posting);
            }
        }
    }
}
