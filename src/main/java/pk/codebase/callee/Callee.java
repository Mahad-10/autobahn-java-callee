package pk.codebase.callee;

import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.types.ExitInfo;

import java.util.concurrent.CompletableFuture;

public class Callee {
    public static void main(String[] args) {
        String url = args[0];
        String procedure = args[1];
        connect(url, procedure);
    }

    private static int connect(String url, String procedure) {
        Session wampSession = new Session();
        wampSession.addOnJoinListener((session, details) -> {
            System.out.println(details);
            session.register(procedure, o -> {
                return "ABU";
            }).whenComplete((registration, throwable) -> {
                System.out.println("Registered,,,,,");
            });
        });

        Client client = new Client(wampSession, url, "realm1");
        CompletableFuture<ExitInfo> exitFuture = client.connect();

        try {
            ExitInfo exitInfo = exitFuture.get();
            return exitInfo.code;
        } catch (Exception e) {
            return 1;
        }
    }
}
