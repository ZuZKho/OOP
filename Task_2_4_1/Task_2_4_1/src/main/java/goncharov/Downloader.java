package goncharov;

public class Downloader {

    private String storagePath = "";

    public Downloader(String path) {
        storagePath = path;
    }

    public boolean download(String repo, String subfolder) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", repo, storagePath + "/" + subfolder);
            Process process = processBuilder.start();
            int exitCode= process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            System.out.println("Can't download repository " + repo + ": " + e);
            return false;
        }
    }
}
