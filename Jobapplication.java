public class Jobapplication {
    private String jobid;
    private String email;

    public Jobapplication(String jobid, String email) {
        this.jobid = jobid;
        this.email = email;
    }

    public String getJobId() {
        return jobid;
    }

    public String getJobseekeremail() {
        return email;
    }
}
