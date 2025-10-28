public class Resume {
    private String education;
    private String skills;
    private int experience;

    public String getEducation() {
        return education;
    }
    public String getSkills() {
        return skills;
    }
    public int getExperience() {
        return experience;
    }
    public Resume(String education, String skills, int experience) {
        this.education = education;
        this.skills = skills.toLowerCase();
        this.experience = experience;
    }
    @Override
    public String toString() {
        String format = "%-15s %s%n";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format, "Education:", this.education));
        sb.append(String.format(format, "Skills:", this.skills));
        sb.append(String.format(format, "Experience:", this.experience + " Years"));

        return sb.toString();
    }
}
