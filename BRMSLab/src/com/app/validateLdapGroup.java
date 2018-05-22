package com.app;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;

import com.cisco.brmspega.util.ldap.LdapUtil;

import com.app.LdapUtilLocal;
public class validateLdapGroup {

	public static void main(String[] args) throws NamingException {
		// TODO Auto-generated method stub

		String submit ="List Ldap Group";
		String ldapGroupName ="brms_prod_cpe_lme_usr";
		//ldapGroupName="brms_stage_cpe_sr_lti_mngr";
		//ldapGroupName="brms_dev_hr_admin";
		ldapGroupName="brms_prod_cpe_sr_user";
		//ldapGroupName="brms_prod_cpe_sr_lme_usr";
		//String ldapGroupName = "brmservice";
		
		List<String> users = null;
		if ((submit != null && submit.equals("List Ldap Group")) || ldapGroupName != null) {
			//ldapGroupName = request.getParameter("lGN");
			
			//LdapUtil ldapUtil = new LdapUtil();
			LdapUtilLocal ldapUtil = new LdapUtilLocal();
			users = ldapUtil.getSearchBasedUserIds(ldapGroupName, "OU=Groups,O=cco.cisco.com", SearchControls.SUBTREE_SCOPE, false);
		}
		
		System.out.println("Total Users:"+users.size());
		if (users != null && users.size() > 0) {
			System.out.println("USER UNDER LDAP GROUP ::"+ldapGroupName);
			int count = 0;
			
			for (String user : users) {
				count = count + 1;
				System.out.println(count + ":" +user);
			}
		}
		
	}

}
