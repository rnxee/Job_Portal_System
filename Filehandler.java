import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Filehandler {

    // Simple CSV helpers: escape fields and parse a CSV line with quotes
    private static String escapeCsv(String s) {
        if (s == null)
            return "";
        String escaped = s.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private static String[] parseCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    // possible escape
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip next
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    parts.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }

    // Save methods for employers
    public static void saveEmployers(String filename, List<Employer> employers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Employer emp : employers) {
                writer.write(escapeCsv(emp.getUser()) + "," + escapeCsv(emp.password));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving employers: " + e.getMessage());
        }
    }

    // Save methods for job seekers
    public static void saveJobSeekers(String filename, List<Jobseeker> seekers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Jobseeker seeker : seekers) {
                Resume r = seeker.getResumeObject();
                if (r != null) {
                    writer.write(escapeCsv(seeker.getUser()) + "," + escapeCsv(seeker.password) + ","
                            + escapeCsv(r.getEducation()) + "," + escapeCsv(r.getSkills())
                            + "," + escapeCsv(Integer.toString(r.getExperience())));
                } else {
                    writer.write(escapeCsv(seeker.getUser()) + "," + escapeCsv(seeker.password) + "," + escapeCsv("")
                            + "," + escapeCsv("") + "," + escapeCsv(""));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving job seekers: " + e.getMessage());
        }
    }

    // Save methods for jobs
    public static void saveJobs(String filename, List<Job> jobs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Job job : jobs) {
                // include postedBy as the last field
                writer.write(escapeCsv(job.getJobTitle()) + "," + escapeCsv(job.getCompany()) + ","
                        + escapeCsv(job.getLocation()) + "," + escapeCsv(Double.toString(job.getSalary()))
                        + "," + escapeCsv(job.getBenefit()) + "," + escapeCsv(job.getRequiredSkills()) + ","
                        + escapeCsv(job.getPostedBy()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving jobs: " + e.getMessage());
        }
    }

    // Save methods for job applications
    public static void saveApplications(String filename, List<Jobapplication> applications) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Jobapplication app : applications) {
                writer.write(escapeCsv(app.getJobId()) + "," + escapeCsv(app.getJobseekeremail()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }

    // Load methods for different entities
    public static List<Employer> loadEmployers(String filename) {
        List<Employer> employers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length == 2) {
                    Employer emp = new Employer(data[0], data[1]);
                    employers.add(emp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading employers: " + e.getMessage());
        }
        return employers;
    }

    // Load job seekers and, their resumes
    public static List<Jobseeker> loadJobSeekers(String filename) {
        List<Jobseeker> seekers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length == 5) {
                    Jobseeker seeker = new Jobseeker(data[0], data[1]);
                    if (!data[2].isEmpty() && !data[3].isEmpty() && !data[4].isEmpty()) {
                        try {
                            int exp = Integer.parseInt(data[4]);
                            Resume resume = new Resume(data[2], data[3], exp);
                            seeker.setResume(resume);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Warning: invalid experience for job seeker '" + data[0]
                                    + "' — skipping resume: " + data[4]);
                        }
                    }
                    seekers.add(seeker);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading job seekers: " + e.getMessage());
        }
        return seekers;
    }

    // Load jobs
    public static List<Job> loadJobs(String filename, List<Employer> employers) {
        List<Job> jobs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                // expected 7 fields now: title,company,location,salary,benefit,skills,postedBy
                if (data.length == 7) {
                    try {
                        double sal = Double.parseDouble(data[3]);
                        Job job = new Job(data[0], data[1], data[2], sal, data[4], data[5], data[6]);
                        jobs.add(job);
                        // associate to employer if found
                        for (Employer emp : employers) {
                            if (emp.getUser().equals(job.getPostedBy())) {
                                emp.getJobsPosted().add(job);
                                break;
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Warning: skipping job with invalid salary: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading jobs: " + e.getMessage());
        }
        return jobs;
    }

    // Load job applications
    public static List<Jobapplication> loadApplications(String filename) {
        List<Jobapplication> applications = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length == 2) {
                    Jobapplication app = new Jobapplication(data[0], data[1]);
                    applications.add(app);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
        return applications;
    }

    // Save and load interviews
    public static void saveInterviews(String filename, List<Interview> interviews) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Interview it : interviews) {
                writer.write(it.getJobTitle() + "," + it.getJobseekerEmail() + "," + it.getDate() + "," + it.getTime()
                        + "," + it.getPlatform());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving interviews: " + e.getMessage());
        }
    }

    // Load interviews
    public static List<Interview> loadInterviews(String filename) {
        List<Interview> interviews = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length == 5) {
                    Interview it = new Interview(data[0], data[1], data[2], data[3], data[4]);
                    interviews.add(it);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading interviews: " + e.getMessage());
        }
        return interviews;
    }

    // Save all data at once
    public void saveAllData(String empFile, String seekerFile, String jobFile, String appFile, List<Employer> employers,
            List<Jobseeker> seekers, List<Job> jobs, List<Jobapplication> applications) {
        saveEmployers(empFile, employers);
        saveJobSeekers(seekerFile, seekers);
        saveJobs(jobFile, jobs);
        saveApplications(appFile, applications);
    }
}
