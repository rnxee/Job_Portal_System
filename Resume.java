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

    /* Polymorphism Compile-Time (Overloading)
       In this part we overloaded the constructor of the Resume class
       to allow us tp create Resume objects with different sets of parameters. */
    public Resume(String education, String skills) {
        this.education = education;
        this.skills = skills.toLowerCase();
    }

    public Resume(String education, String skills, int experience) {
        this.education = education;
        this.skills = skills.toLowerCase();
        this.experience = experience;
    }

    /* Polymorphism Run-Time (Overriding)
       In this part we override the toString() method to provide a custom string representation
       of the Resume object when it is printed or converted to a string. */
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
