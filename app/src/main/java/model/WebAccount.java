package model;

public class WebAccount {
    private String domain;
    private String user;
    private String password;

    public WebAccount()
    {
        this.domain = "";
        this.user = "";
        this.password = "";
    }

    public WebAccount(String domain, String user, String password)
    {
        super();
        this.domain = domain;
        this.user = user;
        this.password = password;
    }

    public String getDomain()
    {
        return domain;
    }

    public String getUser() {
        return user;
    }

    public String toString() {
        return (domain);
    }

    public String getPassword() {
        return password;
    }
}
