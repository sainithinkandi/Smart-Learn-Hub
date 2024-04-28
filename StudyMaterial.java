import java.util.HashMap;
import java.util.Map;

public class StudyMaterial {
    private Map<String, Map<String, String>> subjects;

    public StudyMaterial() {
        subjects = new HashMap<>();
    }

    public String[] getBranches() {
        return subjects.keySet().toArray(new String[0]);
    }

    public String[] getSubjects(String branch) {
        Map<String, String> branchSubjects = subjects.get(branch);
        if (branchSubjects != null) {
            return branchSubjects.keySet().toArray(new String[0]);
        }
        return new String[0];
    }

    public void addStudyMaterial(String branch, String subject, String chapter, String content) {
        Map<String, String> branchSubjects = subjects.get(branch);
        if (branchSubjects == null) {
            branchSubjects = new HashMap<>();
            subjects.put(branch, branchSubjects);
        }

        branchSubjects.put(subject, chapter + ": " + content);
    }

    public String getStudyMaterial(String branch, String subject) {
        Map<String, String> branchSubjects = subjects.get(branch);
        if (branchSubjects != null) {
            return branchSubjects.get(subject);
        }
        return null;
    }
}
