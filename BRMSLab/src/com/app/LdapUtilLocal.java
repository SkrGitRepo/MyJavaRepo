package com.app;



import com.cisco.brmspega.bundles.PropertyLoader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapUtilLocal {
	private DirContext ctx = null;
	  private String[] searchAttrs = { "member" };
      private ArrayList userGroups;
      private ArrayList users;
      private Object enumeration;
	  public static final String strEnv = "prod";//System.getProperty("cisco.life");
	  //private static final String principalUser = "uid=brm.gen,OU=Generics,O=cco.cisco.com";//PropertyLoader.getInstance().getProperty("ldap_principal_user");
	  //private static final String principalPassword = "brmGen123";//PropertyLoader.getInstance().getProperty("ldap_principal_password");
	  static String genID= "ibpmldap.gen"; //ibpmldap.gen
	  static String genIDPwd="ibpmLdap_123";//ibpmLab_123
	  private static final String principalUser = "uid="+genID+",OU=Generics,O=cco.cisco.com";
	  //private static final String principalUser = "uid="+genID+",OU=Generics,O=dsxdev.cisco.com";
	  //PropertyLoader.getInstance().getProperty("ldap_principal_user");
	  private static final String principalPassword = genIDPwd;//"Ibpm-1234";//PropertyLoader.getInstance().getProperty("ldap_principal_password");
	  public static final String ldapUrl;
	  public static int ldap_count;
  
  static
  {
    if ((strEnv != null) && (strEnv.equalsIgnoreCase("PROD"))) {
    	System.out.println("Production :: "+ strEnv+" :: LDAP");
      ldapUrl = "ldap://dsx.cisco.com:389";
    } else if ((strEnv != null) && (strEnv.equalsIgnoreCase("STAGE"))) {
    	System.out.println("non-pord :: "+ strEnv+" :: LDAP");
        ldapUrl = "ldap://dsxstage.cisco.com:389";
     } else {
    	System.out.println("non-pord :: " +strEnv +" :: LDAP");
      ldapUrl = "ldap://dsxdev.cisco.com:389";
    }
  }
  
  public DirContext getDirContext()
  {
    DirContext dirCtx = null;
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
      dirCtx = new InitialDirContext(env);
    }
    catch (NamingException e)
    {
      e.printStackTrace();
    }
    return dirCtx;
  }
  
  public void initiateCtx()
  {
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
    catch (NamingException e)
    {
      e.printStackTrace();
    }
  }
  
 /* public List<String> getUserGroups(String userId)
    throws NamingException
  {
    userGroups = new ArrayList();
    try
    {
      initiateCtx();
      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(2);
      NamingEnumeration<SearchResult> searchResultEnum = this.ctx.search("O=cco.cisco.com", "uid=" + userId, searchControls);
      SearchResult searchResult = null;
      Attributes attributes = null;
      NamingEnumeration<?> groupDnsNamEnum = null;
      String groupDns = null;
      String[] tokens = null;
      String[] subTokens = null;
      while (searchResultEnum.hasMore())
      {
        searchResult = (SearchResult)searchResultEnum.next();
        attributes = searchResult.getAttributes();
        groupDnsNamEnum = attributes.get("memberOf").getAll();
        while (groupDnsNamEnum.hasMore())
        {
          groupDns = (String)groupDnsNamEnum.next();
          tokens = groupDns.split(",");
          for (int i = 0; i < tokens.length; i++)
          {
            subTokens = tokens[i].split("=");
            if ((subTokens.length == 2) && ("CN".equalsIgnoreCase(subTokens[0])))
            {
              userGroups.add(subTokens[1]);
              break;
            }
          }
        }
      }
      return userGroups;
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
  }*/
  
 /* public List<String> getUserIds(String userGroup)
    throws NamingException
  {
    ArrayList users = new ArrayList();
    try
    {
      initiateCtx();
      Attributes attributes = this.ctx.getAttributes("CN=" + userGroup + ",OU=Groups,O=cco.cisco.com", this.searchAttrs);
      if (attributes != null)
      {
        Attribute attr = attributes.get("member");
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
              users.add(subTokens[1]);
              break;
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
  }*/
  
  public List<String> getSearchBasedUserIds(String userGroup, String context, int scope, boolean userGroupInd)
    throws NamingException
  {
    users = new ArrayList();
    //users.ensureCapacity(11000000);
    try
    {
      initiateCtx();
      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(scope);
      //NamingEnumeration<SearchResult> searchResultEnum = this.ctx.search(context, "CN=" + userGroup +" member;range=1-1000", searchControls);
      NamingEnumeration<SearchResult> searchResultEnum = this.ctx.search(context, "CN=" + userGroup, searchControls);
      SearchResult searchResult = null;
      Attributes attributes = null;
      while (searchResultEnum.hasMore())
      {
        searchResult = (SearchResult)searchResultEnum.next();
        System.out.println("****************SERACH result "+searchResult);
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
              System.out.println("*********"+userDns);
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
  
  /*public List<String> getSearchBasedUserIds(String userGroup, String context, int scope, boolean userGroupInd, DirContext ctx)
    throws NamingException
  {
    List<String> users = new ArrayList();
    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(scope);
    NamingEnumeration<SearchResult> searchResultEnum = ctx.search(context, "CN=" + userGroup, searchControls);
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
  }*/
  
  /*public List<String> getSearchBasedUserGroups(String userGroup)
    throws NamingException
  {
    userGroups = new ArrayList();
    try
    {
      initiateCtx();
      SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(2);
      NamingEnumeration<SearchResult> searchResultEnum = this.ctx.search("OU=Groups,O=cco.cisco.com", "CN=" + userGroup, searchControls);
      SearchResult searchResult = null;
      Attributes attributes = null;
      while (searchResultEnum.hasMore())
      {
        searchResult = (SearchResult)searchResultEnum.next();
        attributes = searchResult.getAttributes();
        if (attributes != null) {
          userGroups.add((String)attributes.get("cn").get(0));
        }
      }
      return userGroups;
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
  }*/
  
  /*public NamingEnumeration<SearchResult> getUserDetails(String userId)
    throws NamingException
  {
    enumeration = null;
    try
    {
      initiateCtx();
      SearchControls ctrl = new SearchControls();
      ctrl.setSearchScope(2);
      return this.ctx.search("O=cco.cisco.com", "uid=" + userId, ctrl);
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
  
  public NamingEnumeration<SearchResult> getUserDetails(String userId, DirContext ctx)
    throws NamingException
  {
    NamingEnumeration<SearchResult> enumeration = null;
    try
    {
      SearchControls ctrl = new SearchControls();
      ctrl.setSearchScope(2);
      enumeration = ctx.search("O=cco.cisco.com", "uid=" + userId, ctrl);
    }
    catch (NamingException e)
    {
      e.printStackTrace();
    }
    return enumeration;
  }
  
  public void sampleTest()
    throws NamingException
  {
    initiateCtx();
    SearchControls ctrl = new SearchControls();
    
    ctrl.setSearchScope(2);
    NamingEnumeration<SearchResult> enumeration = this.ctx.search("O=cco.cisco.com", "uid=skatare", ctrl);
    System.out.println("Found : " + enumeration.toString());
    int i = 1;
    SearchResult result = null;
    Attributes attribs = null;
    NamingEnumeration<String> ids = null;
    while (enumeration.hasMore())
    {
      System.out.println("Details of record number : " + i++);
      result = (SearchResult)enumeration.next();
      attribs = result.getAttributes();
      ids = attribs.getIDs();
      while (ids.hasMore()) {
        System.out.println("\t" + attribs.get((String)ids.next()));
      }
    }
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
    
    Attributes attrs = this.ctx.getAttributes("CN=tkl-50k-check,OU=Groups,O=cco.cisco.com");
    ids = attrs.getIDs();
    while (ids.hasMore()) {
      System.out.println("\t" + attrs.get((String)ids.next()));
    }
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
    
    attrs = this.ctx.getAttributes("CN=brmservice,OU=Groups,O=cco.cisco.com");
    ids = attrs.getIDs();
    while (ids.hasMore()) {
      System.out.println("\t" + attrs.get((String)ids.next()));
    }
  }
  
  public static void main1(String[] args)
    throws Exception
  {
    System.out.println(LdapCache.getInstance().getLdapData("Preikows"));
  }
  
  public static void main(String[] args)
    throws Exception
  {
    System.out.println(LdapCache.getInstance().getLdapData("mreckleb"));
    System.out.println(LdapCache.getInstance().getLdapData("mreckleb").getLdapGroups());
  }*/
}
