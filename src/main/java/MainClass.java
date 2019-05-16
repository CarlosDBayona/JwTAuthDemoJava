import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ResourceConfig;
import configuration.resourceConfiguration.MyResourceConfig;

import java.net.URI;

public class MainClass {

    private static final URI _URI = URI.create("http://localhost:8080/");   // change with your own ip address.

    public static void main(String[] args) {
        ResourceConfig resourceConfig = new MyResourceConfig();
        try {
            GrizzlyServerFactory.createHttpServer(_URI, resourceConfig);
               System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", _URI));
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
