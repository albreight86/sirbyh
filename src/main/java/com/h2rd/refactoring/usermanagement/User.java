package com.h2rd.refactoring.usermanagement;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User
{
   private static final Logger log = LogManager.getLogger(User.class.getName());

   public final static String NO_NAME = "NULL_USER_NAME";
   public final static String NO_EMAIL = "NUL_USER_EMAIL";
   public final static User NULL_USER = new User(NO_NAME, NO_EMAIL, new ArrayList<String>());

   private String name;
   private String email;
   private List<String> roles;

   public User()
   { }

   public User(String name, String email, List<String> roles)
   {
      this.name = name;
      this.email = email;
      this.roles = roles;
   }

   public String getName()
   {
      return name;
   }

   public String getEmail()
   {
      return email;
   }

   public List<String> getRoles()
   {
      return roles;
   }

   @Override
   public String toString()
   {
      return "User [name=" + name + ", email=" + email + ", roles=" + roles + "]";
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((email == null) ? 0 : email.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((roles == null) ? 0 : roles.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      User other = (User) obj;
      if (email == null)
      {
         if (other.email != null)
            return false;
      } else if (!email.equals(other.email))
         return false;

      if (name == null)
      {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (roles == null)
      {
         if (other.roles != null)
            return false;
      } else if (!roles.equals(other.roles))
         return false;
      return true;
   }

}
