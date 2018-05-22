package com.sample.utility;

	
	import java.util.Hashtable;
	import javax.naming.*;
	import javax.naming.directory.*;

	public class ADPasswordUpdate {
	   public static void main (String[] args) {
	      Hashtable<String,String> env = new Hashtable<String, String>();
	      String genID = "test-nprd.gen";
	      String userName = "CN="+genID+",OU=Generics,OU=Cisco Users,DC=cisco,DC=com";
	      String oldPassword = "cisco_1234";
	      String newPassword = "Cisco_1234";

	      env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");

	      env.put(Context.SECURITY_AUTHENTICATION,"simple");
	      env.put(Context.SECURITY_PRINCIPAL,userName);
	      env.put(Context.SECURITY_CREDENTIALS,oldPassword);
	      env.put(Context.PROVIDER_URL,"ldaps://ds.cisco.com"); 
	      try {
	            DirContext ctx = new InitialDirContext(env);
	            ModificationItem[] mods = new ModificationItem[2];

	            String oldQuotedPassword = "\"" + oldPassword + "\"";
	            byte[] oldUnicodePassword = oldQuotedPassword.getBytes("UTF-16LE");
	            String newQuotedPassword = "\"" + newPassword + "\"";
	            byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");

	            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, 
	                  new BasicAttribute("unicodePwd", oldUnicodePassword));
	            mods[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE, 
	                  new BasicAttribute("unicodePwd", newUnicodePassword));

	            ctx.modifyAttributes(userName, mods);
	            System.out.println("Changed Password SUCCESSFULLY for: " + userName); 
	            ctx.close();
	      } catch (NamingException e) {
	         System.err.println("Password COULD NOT be CHANGED: " + e);
	      } catch (Exception e) {
	         System.err.println("Other Problem : " + e);
	      }
	   }
	}

