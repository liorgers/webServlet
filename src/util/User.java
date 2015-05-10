package util;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Users")
public class User implements Serializable{

	private static final long serialVersionUID = 6297385302078200511L;

	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private String email;
	@DatabaseField
	private String password;
	@DatabaseField
	private String name;
	@DatabaseField
	private String country;

	//----------------------constructors-------------------------
	public User(){
		// empty constructor required by ORMLite - keep empty
	}

	public User(String em, String pass, String name,String country){
		this.email=em;
		this.password=pass;
		this.name=name;
		this.country=country;
	}


	//----------------------setters-------------------------
	public void setName(String name) {
		this.name = name;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setPassword(String newPass) {
		this.password = newPass;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	//----------------------getters-------------------------
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	public String getCountry() {
		return this.country;
	}

	@Override
	public String toString(){
		return "Name="+this.name+", Email="+this.email+", Country="+this.country;
	}
}
