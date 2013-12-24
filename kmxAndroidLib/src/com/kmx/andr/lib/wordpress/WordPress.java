package com.kmx.andr.lib.wordpress;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

public class WordPress {
	
	public String xmlRPCUrl = "http://counterbolt.com/blog/xmlrpc.php";
	public String adminUsername = "kumar";
	public String adminPassword = "biochips";
	private XMLRPCClient xmlRpcClient;

	public WordPress() {
		urlTester();
	}

	private void urlTester() {
		// TODO Auto-generated method stub
		System.out.println("Testing..."+this.xmlRPCUrl);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this.xmlRPCUrl);
		//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response;
		
		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream res = entity.getContent();
			String data = "Response: ";
			while (res.available()>0) {
				data += res.read();
			}
			System.out.println(data);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public WordPress(String xmlRPCUrl) {
		this.xmlRPCUrl = xmlRPCUrl;
		urlTester();
	}

	public WordPress(String xmlRPCUrl, String adminUsername, String adminPassword) {
		this.xmlRPCUrl = xmlRPCUrl;
		this.adminUsername = adminUsername;
		this.adminPassword = adminPassword;
		urlTester();
	}

	public boolean connect() {
		xmlRpcClient = new XMLRPCClient(this.xmlRPCUrl);
		boolean res = call("wp.getUsers",getBaseParam()).length>0;
		System.out.println("XMLRPC Ready!");
		return res;
		
	}
	
	public boolean createUser(String username, String password, String email){
		
		Vector<String> param = getBaseParam();
		param.add(username);
		param.add(password);
		param.add(email);
		Object[] responses = call("wp.getUsers",param);
		System.out.println(responses.toString());
		return true;
		
	}
	
	public ArrayList<User> getUsers(){
		ArrayList<User> users = new ArrayList<User>();
		Vector<String> param = getBaseParam();
		Object[] responses = call("wp.getUsers",param);

		for (Object eachResponse : responses) {
            @SuppressWarnings("unchecked")
			HashMap<String,Object> map = (HashMap<String,Object>) eachResponse;
            
            User u = new User();
            u.user_id = Integer.parseInt(map.get("user_id").toString());
            u.username = map.get("username").toString();
            u.first_name = map.get("first_name").toString();
            u.last_name = map.get("last_name").toString();
            u.bio = map.get("bio").toString();
            u.email = map.get("email").toString();            
            u.nickname = map.get("nickname").toString();
            u.url = map.get("url").toString();            
            u.display_name = map.get("display_name").toString();
            u.registered = map.get("registered").toString();
            u.isValid = true;
            
            users.add(u);
                        
    	}
		
		return users;
		
	}
	
	@SuppressWarnings("unchecked")
	public User getUser(String name, String password) {
        User u = new User();
		XMLRPCClient tmp = new XMLRPCClient(this.xmlRPCUrl);
		
        Vector<String> parameters = new Vector<String>();
        parameters.add("0");
        parameters.add(name);
        parameters.add(password);
        
		Object resp = null;
		try {
			resp = tmp.call("wp.getProfile", parameters.toArray());
		} catch (XMLRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(resp!=null) {
		HashMap<String,Object> map = (HashMap<String,Object>) resp;

        u.user_id = Integer.parseInt(map.get("user_id").toString());
        u.username = map.get("username").toString();
        u.first_name = map.get("first_name").toString();
        u.last_name = map.get("last_name").toString();
        u.bio = map.get("bio").toString();
        u.email = map.get("email").toString();            
        u.nickname = map.get("nickname").toString();
        u.url = map.get("url").toString();            
        u.display_name = map.get("display_name").toString();
        u.registered = map.get("registered").toString();		
        u.isValid = true;
		}
		
		return u;
	}
	
	//---------------------------
	private Object[] call(String methodName, Vector<String> parameters) {
		Object[] responses=null;
        try {
			responses = (Object[])  xmlRpcClient.call(methodName, parameters.toArray());
		} catch (XMLRPCException e) {
			e.printStackTrace();
		}
        return responses;
	}
	
	private Vector<String> getBaseParam() {
        Vector<String> parameters = new Vector<String>();
        parameters.add("0");
        parameters.add(this.adminUsername);
        parameters.add(new String(this.adminPassword));
        return parameters;
	}
	
}
