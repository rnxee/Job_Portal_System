public class Job {
    private String jobTitle;
    private String company;
    private String location;
    private double salary;
    private String benefit;
    private String requiredSkills;
    private String postedBy;

    public Job(String jobTitle, String company, String location, double salary, String benefit, String requiredSkills,
            String postedBy) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.benefit = benefit;
        this.requiredSkills = requiredSkills.toLowerCase();
        this.postedBy = postedBy;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public double getSalary() {
        return salary;
    }

    public String getBenefit() {
        return benefit;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public String getPostedBy() {
        return postedBy;
    }

    @Override
    public String toString() {
        String format = "%-18s %s%n";

        StringBuilder sb = new StringBuilder();
        sb.append("\n=========================================\n");
        sb.append(String.format("JOB POSTING: %s%n", jobTitle));
        sb.append("-----------------------------------------\n");

        sb.append(String.format(format, "Company:", company));
        sb.append(String.format(format, "Location:", location));
        sb.append(String.format(format, "Salary:", salary));
        sb.append(String.format(format, "Benefits:", benefit));
        sb.append(String.format(format, "Skills Required:", requiredSkills));

        sb.append("=========================================\n");

        return sb.toString();
    }
}
