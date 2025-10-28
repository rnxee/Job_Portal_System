import java.util.*;
import java.util.List;

public class Jobportalsystem {
    private static final String EMPLOYER_FILE = "Employer.txt";
    private static final String JOBSEEKER_FILE = "Jobseeker.txt";
    private static final String JOB_FILE = "Job.txt";
    private static final String APPLICATION_FILE = "Jobapplication.txt";

    private List<Employer> employers;
    private List<Jobseeker> jobSeekers;
    private List<Job> jobs;
    private List<Jobapplication> applications;
    private List<Interview> interviews;
    private Scanner scanner;

    public Jobportalsystem() {
        employers = Filehandler.loadEmployers(EMPLOYER_FILE);
        jobSeekers = Filehandler.loadJobSeekers(JOBSEEKER_FILE);
        jobs = Filehandler.loadJobs(JOB_FILE, employers);
        applications = Filehandler.loadApplications(APPLICATION_FILE);
        interviews = Filehandler.loadInterviews("Interview.txt");
        scanner = new Scanner(System.in);
    }
    // Main menu
    public void start() {
        while (true) {
            System.out.println("\n--- Job Portal ---");
            System.out.println("1. Register as Employer");
            System.out.println("2. Register as Job Seeker");
            System.out.println("3. Login as Employer");
            System.out.println("4. Login as Job Seeker");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice;
            while (true) {
                String line = scanner.nextLine();
                try {
                    choice = Integer.parseInt(line.trim());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.print("\nInvalid input. Please enter a number: ");
                }
            }

            switch (choice) {
                case 1 -> registerEmployer();
                case 2 -> registerJobSeeker();
                case 3 -> loginEmployer();
                case 4 -> loginJobSeeker();
                case 0 -> {
                    saveData();
                    System.out.println("Thank you for using Job Portal!");
                    return;
                }
                default -> System.out.println("Invalid choice! Please Try Again.");
            }
        }
    }
    // Registrations
    private void registerEmployer() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        Employer employer = new Employer(user, password);
        employers.add(employer);
        Filehandler.saveEmployers(EMPLOYER_FILE, employers);
        System.out.println("Employer registered successfully!");
    }

    private void registerJobSeeker() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Jobseeker seeker = new Jobseeker(user, password);
        seeker.createResume();

        jobSeekers.add(seeker);
        Filehandler.saveJobSeekers(JOBSEEKER_FILE, jobSeekers);
        System.out.println("Job Seeker registered successfully!");
    }
    // Logins
    private void loginEmployer() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (Employer e : employers) {
            if (e.getUser().equals(user) && e.validatePassword(password)) {
                System.out.println("Welcome " + user + "!");
                employerMenu(e);
                return;
            }
        }
        System.out.println("Invalid!");
    }

    private void loginJobSeeker() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (Jobseeker js : jobSeekers) {
            if (js.getUser().equals(user) && js.validatePassword(password)) {
                System.out.println("Welcome " + user + "!");
                jobSeekerMenu(js);
                return;
            }
        }
        System.out.println("Invalid!");
    }
    // Employer Menu
    private void employerMenu(Employer employer) {
        while (true) {
            System.out.println("\n--- Employer Menu ---");
            System.out.println("1. Post a Job");
            System.out.println("2. View Applicants");
            System.out.println("3. Schedule Interview");
            System.out.println("0. Logout");
            System.out.print("Choose option: ");
            int choice;
            while (true) {
                String line = scanner.nextLine();
                try {
                    choice = Integer.parseInt(line.trim());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.print("Invalid input. Please enter a number: ");
                }
            }

            switch (choice) {
                case 1 -> employer.postJob(jobs);
                case 2 -> employer.viewApplicants();
                case 3 -> employer.scheduleInterview(interviews, jobs);
                case 0 -> {
                    saveData();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    // Job Seeker Menu
    private void jobSeekerMenu(Jobseeker seeker) {
        while (true) {
            System.out.println("\n--- Job Seeker Menu ---");
            System.out.println("1. View All Jobs");
            System.out.println("2. Apply for a Job");
            System.out.println("3. Find Suggested Jobs");
            System.out.println("4. View My Resume");
            System.out.println("5. View My Interviews");
            System.out.println("0. Logout");

            System.out.print("Choose option: ");
            int choice;
            while (true) {
                String line = scanner.nextLine();
                try {
                    choice = Integer.parseInt(line.trim());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.print("Invalid input. Please enter a number: ");
                }
            }

            switch (choice) {
                case 1 -> seeker.viewJobs(jobs);
                case 2 -> seeker.applyForJob(employers, jobs);
                case 3 -> seeker.matchJobs(jobs);
                case 4 -> seeker.viewResume();
                case 5 -> seeker.viewInterviews(interviews);

                case 0 -> {
                    saveData();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    // Save all data to files
    private void saveData() {
        Filehandler.saveEmployers(EMPLOYER_FILE, employers);
        Filehandler.saveJobSeekers(JOBSEEKER_FILE, jobSeekers);
        Filehandler.saveJobs(JOB_FILE, jobs);
        Filehandler.saveApplications(APPLICATION_FILE, applications);
        Filehandler.saveInterviews("Interview.txt", interviews);
    }

    public static void main(String[] args) {
        Jobportalsystem system = new Jobportalsystem();
        system.start();
    }
}
