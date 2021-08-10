package io.github.bluething.java.heapdump.klassified.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.github.bluething.java.heapdump.klassified.api.Posting;
import io.github.bluething.java.heapdump.klassified.jdbi.AdvertDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * NB: must be thread-safe
 */
@Path("/postings")
@Produces(MediaType.APPLICATION_JSON)
public class PostingResource
{
    private final PostingCache postingCache = new PostingCache();
    private final AdvertDAO advertDAO;

    public PostingResource(final AdvertDAO advertDAO)
    {
        this.advertDAO = advertDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    public List<Posting> list()
    {
        return advertDAO.find()
                        .stream()
                        .map(postingCache::get)
                        .collect(toList());
    }
}
