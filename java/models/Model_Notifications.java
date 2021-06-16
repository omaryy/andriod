package models;

public class Model_Notifications {
    String  description;
    public  Model_Notifications()
    {
        
    }

    public Model_Notifications(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
