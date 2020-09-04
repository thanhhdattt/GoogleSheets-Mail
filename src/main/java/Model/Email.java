package Model;

public class Email {
    private String address;
    private boolean status = false;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Email(String address, boolean status) {

        this.address = address;
        this.status = status;
    }

    public Email() {
    }
}
