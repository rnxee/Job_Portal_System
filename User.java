import java.util.*;
import java.util.Scanner;

// User class
public class User {
    Scanner scanner = new Scanner(System.in);
    protected String user;
    protected String password;

    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}
// Employer class
class Employer extends User {
    private List<Job> jobsPosted = new ArrayList<>();
    private Map<String, List<String>> jobApplicants = new HashMap<>();

    public Employer(String user, String password) {
        super(user, password);
    }

    public List<Job> getJobsPosted() {
        return jobsPosted;
    }
    // job posting
    public void postJob(List<Job> jobs) {
        System.out.println("\n--- Post a Job ---");
        System.out.print("Job Title: ");
        String jobTitle = scanner.nextLine();
        System.out.print("Company: ");
        String company = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Salary: ");
        double salary;
        while (true) {
            String s = scanner.nextLine();
            try {
                salary = Double.parseDouble(s);
                break;
            } catch (NumberFormatException nfe) {
                System.out.print("Invalid salary. Enter numeric value for Salary: ");
            }
        }
        System.out.print("Benefits: ");
        String benefit = scanner.nextLine();
        System.out.print("Required Skills (comma-separated, e.g., Java, SQL, AWS): ");
        String requiredSkills = scanner.nextLine();
        // Create job and record who posted it
        Job job = new Job(jobTitle, company, location, salary, benefit, requiredSkills, this.getUser());
        jobs.add(job);
        this.jobsPosted.add(job);
        System.out.println("Job posted successfully!");
    }
    // manage applicants
    public void addApplicant(String jobTitle, String seekerEmail) {
        List<String> list = jobApplicants.get(jobTitle);
        if (list == null) {
            list = new ArrayList<>();
            jobApplicants.put(jobTitle, list);
        }
        list.add(seekerEmail);
    }
    // view applicants
    public void viewApplicants() {
        System.out.println("--- Applicants for Your Jobs ---");
        if (jobApplicants.isEmpty()) {
            System.out.println("No Applicants received yet.");
            return;
        }

        for (Map.Entry<String, List<String>> entry : jobApplicants.entrySet()) {
            System.out.println("Job: " + entry.getKey());
            for (String email : entry.getValue()) {
                System.out.println("  - " + email);
            }
        }
    }
    // schedule interview
    public void scheduleInterview(List<Interview> interviews, List<Job> jobs) {
        System.out.println("\n--- Schedule Interview ---");
        System.out.print("Job Title: ");
        String jobTitle = scanner.nextLine();
        Job found = null;
        for (Job j : jobs) {
            if (j.getJobTitle().equalsIgnoreCase(jobTitle) && this.getUser().equals(j.getPostedBy())) {
                found = j;
                break;
            }
        }
        if (found == null) {
            System.out.println("Error: Job not found or you are not the poster of this job.");
            return;
        }

        System.out.print("Job Seeker Email (username): ");
        String email = scanner.nextLine();
        System.out.print("Date (e.g., 2025-10-12): ");
        String date = scanner.nextLine();
        System.out.print("Time (e.g., 14:00): ");
        String time = scanner.nextLine();
        System.out.print("Platform (e.g., Zoom): ");
        String platform = scanner.nextLine();

        Interview it = new Interview(jobTitle, email, date, time, platform);
        interviews.add(it);
        System.out.println("Interview scheduled.");
    }
}
// Jobseeker class
class Jobseeker extends User {
    private Resume resume;

    public Jobseeker(String user, String password) {
        super(user, password);
        this.resume = null;
    }

    public Resume getResumeObject() {
        return resume;
    }

    public String getResume() {
        return resume.toString();
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }
    // create resume
    public void createResume() {
        System.out.println("\n--- Resume Details ---");
        System.out.print("Education: ");
        String education = scanner.nextLine();
        System.out.print("Skills Keywords (comma-separated, e.g., Java, SQL, AWS): ");
        String skills = scanner.nextLine();
        System.out.print("Experience (Years):");
        int experience;
        while (true) {
            String sExp = scanner.nextLine();
            try {
                experience = Integer.parseInt(sExp);
                break;
            } catch (NumberFormatException nfe) {
                System.out.print("Invalid number. Enter Experience (Years) as an integer: ");
            }
        }

        Resume newResume = new Resume(education, skills, experience);
        this.setResume(newResume);
        System.out.println("Resume created and attached to " + this.user + ".");
    }
    // view available jobs
    public void viewJobs(List<Job> jobs) {
        for (Job job : jobs) {
            System.out.println(job);
        }
    }
    // apply for job
    public void applyForJob(List<Employer> employers, List<Job> jobs) {
        System.out.print("Enter Job Title to apply for: ");
        String jobTitle = scanner.nextLine();

        Job foundJob = null;
        Employer jobOwner = null;
        for (Employer emp : employers) {
            for (Job job : emp.getJobsPosted()) {
                if (job.getJobTitle().equalsIgnoreCase(jobTitle)) {
                    foundJob = job;
                    jobOwner = emp;
                    break;
                }
            }
            if (foundJob != null)
                break;
        }

        if (foundJob == null) {
            System.out.println("Error: Job Title does not exist.");
            return;
        }

        jobOwner.addApplicant(jobTitle, this.getUser());
        System.out.println("Application submitted!");
    }
    // view resume
    public void viewResume() {
        if (resume != null) {
            System.out.println("\n--- Your Resume ---");
            System.out.println(resume);
        } else {
            System.out.println("No resume available.");
        }
    }
    // filter and match jobs
    public void matchJobs(List<Job> jobs) {
        if (this.resume == null) {
            System.out.println("Error: You must have a resume to match jobs! Create a resume first.");
            return;
        }
        String[] seekerSkills = this.resume.getSkills().toLowerCase().split(",\\s*");
        System.out.println("\n--- Job Matches for " + this.getUser() + " ---");
        boolean matchFound = false;

        for (Job job : jobs) {
            String[] jobSkills = job.getRequiredSkills().toLowerCase().split(",\\s*");
            for (String skill : seekerSkills) {
                for (String jobSkill : jobSkills) {
                    if (skill.equals(jobSkill)) {
                        System.out.println(job);
                        matchFound = true;
                        break;
                    }
                }
                if (matchFound)
                    break;
            }
        }
        if (!matchFound) {
            System.out.println("No matching jobs found based on your resume skills.");
        }
    }
    // view interviews
    public void viewInterviews(List<Interview> interviews) {
        System.out.println("\n--- Your Interviews ---");
        boolean found = false;
        for (Interview it : interviews) {
            if (it.getJobseekerEmail().equalsIgnoreCase(this.getUser())) {
                System.out.println(it);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No interviews scheduled for you.");
        }
    }

}
