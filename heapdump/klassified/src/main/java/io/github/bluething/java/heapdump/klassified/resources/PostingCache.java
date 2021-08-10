package io.github.bluething.java.heapdump.klassified.resources;

import io.github.bluething.java.heapdump.klassified.api.Posting;
import io.github.bluething.java.heapdump.klassified.jdbi.Advert;

import java.util.HashMap;
import java.util.Map;

public class PostingCache
{
    private final Map<PostingId, Posting> idToPosting = new HashMap<>();

    public Posting get(final Advert advert)
    {
        final PostingId postingId = new PostingId(advert.getId());
        return idToPosting.computeIfAbsent(postingId, key ->
            new Posting(
                advert.getId(),
                advert.getCategory(),
                advert.getTitle(),
                advert.getDescription(),
                advert.getPostedAt().toEpochSecond(),
                advert.getLastModifiedAt().toEpochSecond(),
                advert.getPostedBy().getName(),
                advert.getPostedBy().getContactDetails()));
    }

    private class PostingId
    {
        private final long id;

        private PostingId(final long id)
        {
            this.id = id;
        }

        public int hashCode()
        {
            return Long.hashCode(id);
        }
    }

}
