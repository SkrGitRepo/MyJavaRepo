package com.app;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class DirectBRMSLDAPCall {/*


	  private DirContext ctx = null;
	  private String[] searchAttrs = { "member" };
	  public static final String strEnv = "PROD";//System.getProperty("cisco.life");
	  private static final String principalUser = "uid=brm.gen,OU=Generics,O=cco.cisco.com";//PropertyLoader.getInstance().getProperty("ldap_principal_user");
	  private static final String principalPassword = "brmGen123";//PropertyLoader.getInstance().getProperty("ldap_principal_password");
	  public static final String ldapUrl;
	  public static int ldap_count;
	  
	 static
	  {
	    if ((strEnv != null) && (strEnv.equalsIgnoreCase("PROD"))) {
	      ldapUrl = "ldap://dsx.cisco.com:389";
	    } else {
	      ldapUrl = "ldap://dsxstage.cisco.com:389";
	    }
	  }
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	 public void initiateCtx()   {
	    try
	    {
	      System.out.println(new Date() + "LDAP Connections Count - " + ++ldap_count);
	      Hashtable<String, String> env = new Hashtable();
	      env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
	      env.put("java.naming.provider.url", ldapUrl);
	      env.put("java.naming.security.authentication", "simple");
	      env.put("java.naming.security.principal", principalUser);
	      env.put("java.naming.security.credentials", principalPassword);
	      env.put("java.naming.referral", "throw");
	      this.ctx = new InitialDirContext(env);
	    }
	    catch (NamingException e)   {
	      e.printStackTrace();
	    }
	
	 }
	 

	  public List<String> getSearchBasedUserIds(String userGroup, String context, int scope, boolean userGroupInd)
	    throws NamingException
	  {
	    users = new ArrayList();
	    try
	    {
	      initiateCtx();
	      SearchControls searchControls = new SearchControls();
	      searchControls.setSearchScope(scope);
	      NamingEnumeration<SearchResult> searchResultEnum = this.ctx.search(context, "CN=" + userGroup, searchControls);
	      SearchResult searchResult = null;
	      Attributes attributes = null;
	      while (searchResultEnum.hasMore())
	      {
	        searchResult = (SearchResult)searchResultEnum.next();
	        attributes = searchResult.getAttributes();
	        if (attributes != null)
	        {
	          Attribute attr = attributes.get("member");
	          if (attr != null)
	          {
	            NamingEnumeration<?> userDnsNamEnum = attr.getAll();
	            String userDns = null;
	            String[] tokens = null;
	            String[] subTokens = null;
	            while (userDnsNamEnum.hasMore())
	            {
	              userDns = (String)userDnsNamEnum.next();
	              tokens = userDns.split(",");
	              for (int i = 0; i < tokens.length; i++)
	              {
	                subTokens = tokens[i].split("=");
	                if ((subTokens.length == 2) && ("uid".equalsIgnoreCase(subTokens[0])))
	                {
	                  users.add(subTokens[1] + (userGroupInd ? "," + (String)attributes.get("cn").get(0) : ""));
	                  break;
	                }
	              }
	            }
	          }
	        }
	      }
	      return users;
	    }
	    finally
	    {
	      if (this.ctx != null) {
	        try
	        {
	          this.ctx.close();
	        }
	        catch (NamingException e)
	        {
	          e.printStackTrace();
	        }
	      }
	    }
	  }

*/}
