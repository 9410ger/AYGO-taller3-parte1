package co.edu.escuelaing.security;

import static spark.Spark.*;

public class HelloService {

    public static void main(String[] args){
        port(getPort());

        secure("keystore/ecikeystore.p12", "123456", null, null);
        get("/hello",(req,res) -> "Hello World");
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }


}
