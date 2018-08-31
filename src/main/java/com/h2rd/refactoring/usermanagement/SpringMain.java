package com.h2rd.refactoring.usermanagement;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain
{
   public static void main(String[] args)
   {
      new SpringMain();
   }

   public SpringMain()
   {
       ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application-config.xml");
   }
}
