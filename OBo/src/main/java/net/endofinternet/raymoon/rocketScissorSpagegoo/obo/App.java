package net.endofinternet.raymoon.rocketScissorSpagegoo.obo;

import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Account;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.GameAI;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.GameHandler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       String host = "localhost";
       int port = 6000;
       
       Account account = new Account("ray", "876082740928734987324");
       GameAI ai = new GatlingAI(account.getUsername());
       
       // now beat the shit out of the enemy!
       new GameHandler(ai, account, host, port).run();
    }
}
