package com.kmx.andr.lib.wordpress;

public class User {
      
	  public Integer user_id;
	  public String username;
	  public String first_name;
	  public String last_name;
	  public String bio;
	  public String email;
	  public String nickname;
	  public String nicename;
	  public String url;
	  public String display_name;
	  public String registered;
	  public Boolean isValid=false;
	  
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username
				+ ", first_name=" + first_name + ", last_name=" + last_name
				+ ", bio=" + bio + ", email=" + email + ", nickname="
				+ nickname + ", nicename=" + nicename + ", url=" + url
				+ ", display_name=" + display_name + ", registered="
				+ registered + ", isValid=" + isValid + "]";
	}
	  
}
