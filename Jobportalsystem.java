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
            System.out.println("--- Job Portal ---");
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
                    clearScreen();
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
        System.out.print("\nEmployer registered successfully!");
        scanner.nextLine(); clearScreen();
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
        System.out.print("Job Seeker registered successfully!");
        scanner.nextLine();  clearScreen();
    }
    // Logins
    private void loginEmployer() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (Employer employer : employers) {
            if (employer.getUser().equals(user) && employer.validatePassword(password)) {
                clearScreen();
                System.out.println("Welcome " + user + "!");
                employer.showMenu(jobs, interviews, employers);
                saveData();
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

        for (Jobseeker seeker : jobSeekers) {
            if (seeker.getUser().equals(user) && seeker.validatePassword(password)) {
                System.out.println("Welcome " + user + "!");
                seeker.showMenu(jobs, interviews, employers);
                saveData();
                return;
            }
        }
        System.out.println("Invalid!");
    }

    // Save all data to files
    private void saveData() {
        Filehandler.saveEmployers(EMPLOYER_FILE, employers);
        Filehandler.saveJobSeekers(JOBSEEKER_FILE, jobSeekers);
        Filehandler.saveJobs(JOB_FILE, jobs);
        Filehandler.saveApplications(APPLICATION_FILE, applications);
        Filehandler.saveInterviews("Interview.txt", interviews);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Jobportalsystem system = new Jobportalsystem();
        clearScreen();
        system.start();
    }
}
