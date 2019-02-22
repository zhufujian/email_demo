

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class DubboMain{
    public static void main(String[] args) throws IOException
    {            
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "dubbo-provider.xml" });
        context.start();          
        System.out.println("server is started...");        
        System.in.read();                                                                                                    
    }                                         
}      
         