package io.github.bluething.java.heapdump.klassified;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.bluething.java.heapdump.klassified.jdbi.Advert;
import io.github.bluething.java.heapdump.klassified.jdbi.AdvertDAO;
import io.github.bluething.java.heapdump.klassified.jdbi.Advertiser;
import io.github.bluething.java.heapdump.klassified.jdbi.AdvertiserDAO;
import io.github.bluething.java.heapdump.klassified.resources.PopulateResource;
import io.github.bluething.java.heapdump.klassified.resources.PostingResource;

public class KlassifiedApplication extends Application<KlassifiedConfiguration>
{
    private final HibernateBundle<KlassifiedConfiguration> hibernate = new HibernateBundle<KlassifiedConfiguration>(
        Advert.class, Advertiser.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(KlassifiedConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

    public static void main(String[] args) throws Exception
    {
        new KlassifiedApplication().run(args);
    }

    @Override
    public String getName()
    {
        return "klassified";
    }

    @UnitOfWork
    @Override
    public void run(
        final KlassifiedConfiguration klassifiedConfiguration, final Environment environment) throws Exception
    {
        final AdvertDAO advertDAO = getAdvertDAO();
        final AdvertiserDAO advertiserDAO = getAdvertiserDAO();
        final JerseyEnvironment jersey = environment.jersey();

        jersey.register(new PostingResource(advertDAO));
        jersey.register(new PopulateResource(advertDAO, advertiserDAO));
    }

    public AdvertiserDAO getAdvertiserDAO()
    {
        return new AdvertiserDAO(hibernate.getSessionFactory());
    }

    public AdvertDAO getAdvertDAO()
    {
        return new AdvertDAO(hibernate.getSessionFactory());
    }

    @Override
    public void initialize(final Bootstrap<KlassifiedConfiguration> bootstrap)
    {
        bootstrap.addBundle(hibernate);
    }
}
