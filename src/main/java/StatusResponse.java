public enum StatusResponse {
    OK ("200"),
    ACCEPTED ("202"),
    NOT_FOUND ("404"),
    UNAUTHORIZED ("401"),
    FORBIDEN ("403"),
    INTERNAL_SERVER_ERROR ("500");


    private String status;

    StatusResponse(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
