package sample;

public class Employee {
    private int emp_id;
    private String user_name;
    private String password;
    private String email;
    private String start_date;

    public Employee(int id, String username, String password, String email, String date){
        this.emp_id = id;
        this.user_name = username;
        this.password = password;
        this.email = email;
        this.start_date = date;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
