package sadiq.raza.assesszone;

public class AvailableTestDetails {
    private String test_id,test_name,start_date_time,time_allowed;

    public AvailableTestDetails(String test_id, String test_name, String start_date_time, String time_allowed) {
        this.test_id = test_id;
        this.test_name = test_name;
        this.start_date_time = start_date_time;
        this.time_allowed = time_allowed;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTest_name() {
        return test_name;
    }


    public String getStart_date_time() {
        return start_date_time;
}

    public String getTime_allowed() {
        return time_allowed;
    }


}
