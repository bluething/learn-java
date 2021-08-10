package io.github.bluething.java.heapdump.klassified.jdbi;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class AdvertiserDAO extends AbstractDAO<Advertiser>
{
    public AdvertiserDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public long save(final Advertiser advertiser)
    {
        return persist(advertiser).getId();
    }
}
