public class Interview {
    private String jobTitle;
    private String jobseekerEmail;
    private String date;
    private String time;
    private String platform;
        
    public Interview(String jobTitle, String jobseekerEmail, String date, String time, String platform) {
        this.jobTitle = jobTitle;
        this.jobseekerEmail = jobseekerEmail;
        this.date = date;
        this.time = time;
        this.platform = platform;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobseekerEmail() {
        return jobseekerEmail;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        String format = "%-15s %s%n";

        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------\n");
        sb.append(String.format("INTERVIEW for: %s%n", jobTitle));
        sb.append(String.format(format, "Job Seeker:", jobseekerEmail));
        sb.append(String.format(format, "Date:", date));
        sb.append(String.format(format, "Time:", time));
        sb.append(String.format(format, "Platform:", platform));
        sb.append("-----------------------------------------\n");
        return sb.toString();
    }

}
