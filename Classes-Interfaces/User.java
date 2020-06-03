package generator.nex.rexx.userregistration.Model;

/**
 * Created by rexx on 19-04-2018.
 */

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String Email;
    private String IsStaff;
    //private String UserId;

    public User(){

    }

    public User(String name, String password, String phone, String email) {
        Name = name;
        Password = password;
        Phone = phone;
        Email = email;
        IsStaff = "false";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
