package pk.codebase.callee;

import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.types.ExitInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Callee {
    public static void main(String[] args) {
        connect();
    }

    private static int connect() {
        Session wampSession = new Session();
        wampSession.addOnJoinListener((session, details) -> {
            System.out.println(details);
            session.register("simplethings", o -> {
                return "ABU";
            }).whenComplete((registration, throwable) -> {
                System.out.println("Registered,,,,,");
            });
        });

        Client client = new Client(wampSession, "ws://localhost:8080/ws", "realm1");
        CompletableFuture<ExitInfo> exitFuture = client.connect();

        try {
            ExitInfo exitInfo = exitFuture.get();
            return exitInfo.code;
        } catch (Exception e) {
            return 1;
        }
    }
}
