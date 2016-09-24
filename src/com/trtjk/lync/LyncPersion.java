package com.trtjk.lync;

import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LyncPersion {
	private LdapContext ctx = null;
	private String baseName = ",OU=IT共享中心,DC=sfty,DC=com";

	public LyncPersion() {
//		Properties prop = new Properties();
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("lyncs");
			String INITIAL_CONTEXT_FACTORY = resourceBundle.getString("Context.INITIAL_CONTEXT_FACTORY");   
			String PROVIDER_URL = resourceBundle.getString("Context.PROVIDER_URL");   
			String SECURITY_AUTHENTICATION = resourceBundle.getString("Context.SECURITY_AUTHENTICATION");   
			String SECURITY_PRINCIPAL = resourceBundle.getString("Context.SECURITY_PRINCIPAL");   
			String SECURITY_CREDENTIALS = resourceBundle.getString("Context.SECURITY_CREDENTIALS");   
			System.out.println("进入无参构造。。。");
			Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY,INITIAL_CONTEXT_FACTORY);
			ldapEnv.put(Context.PROVIDER_URL, PROVIDER_URL);
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
			ldapEnv.put(Context.SECURITY_PRINCIPAL,SECURITY_PRINCIPAL);
			ldapEnv.put(Context.SECURITY_CREDENTIALS,SECURITY_CREDENTIALS); // 密码
			// ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
			ctx = new InitialLdapContext(ldapEnv, null);
			
		} catch (Exception e) {
			throw new RuntimeException("LyncPersion构造错误：", e);
		}
		System.out.println("从无参数构造出来！");
	}

	/**
	 * 更新用户
	 * 
	 * @param username
	 */
	public String update(String username,String userLogin) {
		String returnCode = "";
		ResourceBundle resourceBundle = ResourceBundle.getBundle("lyncupdate");
		String PrimaryHomeServer = resourceBundle.getString("msRTCSIP-PrimaryHomeServer"); 
//		String PrimaryUserAddress = resourceBundle.getString("msRTCSIP-PrimaryUserAddress"); 
		String UserEnabled = resourceBundle.getString("msRTCSIP-UserEnabled"); 
		String OptionFlags = resourceBundle.getString("msRTCSIP-OptionFlags"); 
		String UserPolicies = resourceBundle.getString("msRTCSIP-UserPolicies"); 
		String description = resourceBundle.getString("description"); 
		String DeploymentLocator = resourceBundle.getString("msRTCSIP-DeploymentLocator"); 
		System.out.println("updating...\n");
		ModificationItem[] mods = new ModificationItem[7];
		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("msRTCSIP-PrimaryHomeServer",PrimaryHomeServer)); //
		mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				
				new BasicAttribute("msRTCSIP-PrimaryUserAddress", "sip:"+userLogin+"@sfty.COM"));
		mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				
				new BasicAttribute("msRTCSIP-UserEnabled", UserEnabled));
		mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("msRTCSIP-OptionFlags", OptionFlags));
		mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("msRTCSIP-UserPolicies", UserPolicies));
		mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("description", description));
		mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("msRTCSIP-DeploymentLocator", DeploymentLocator));	
//		for(int i = 0 ; i<mods.length;i++){
//			DirContext.
//		}
		try {
			ctx.modifyAttributes("cn=" + username + baseName, mods);
			returnCode = "000";
		} catch (NamingException e) {
			returnCode = "003,创建失败，在AD组织中未找到该用户";
			e.printStackTrace();
		} catch (Exception e) {
			returnCode = "002,创建失败";
			e.printStackTrace();
		}
		System.out.println("returnCode:"+returnCode);
		return returnCode;
	}

	public static void main(String[] args) {
		LyncPersion adt = new LyncPersion();
		adt.update("北京同仁堂股份有限公司经营分公司","NULL16");
	}
}