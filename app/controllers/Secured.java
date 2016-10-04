package controllers;

        import com.typesafe.config.ConfigFactory;
        import models.HttpAuthUser;
        import play.mvc.Http;
        import play.mvc.Result;
        import play.mvc.Security;

public class Secured extends Security.Authenticator {
    public final static String AUTHORIZATION = "Authorization";
    public final static String USERNAME = ConfigFactory.load().getString("httpauth.username");
    public final static String PASSWORD = ConfigFactory.load().getString("httpauth.password");

    @Override
    public String getUsername(Http.Context context) {
        HttpAuthUser httpAuthUser;
        String[] authTokenHeaderValues = context.request().headers().get(AUTHORIZATION);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            httpAuthUser = HttpAuthUser.findByAuthToken(authTokenHeaderValues[0]);
            if (httpAuthUser != null) {
                if (USERNAME.equals(httpAuthUser.getUsername()) &&
                        PASSWORD.equals(httpAuthUser.getPassword())) {
                    return httpAuthUser.getUsername();
                }
            }
        }

        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }
}
